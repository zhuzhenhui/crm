<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>员工管理</title>
    <#include "../common/link.ftl">
    <script>
        //角色左右移动的效果
        function moveSelected(src, target) {
            $("." + target).append($("." + src + " option:selected"));
        }
        function moveAll(src, target) {
            $("." + target).append($("." + src + " option"));
        }
        $(function () {
            var roleDiv;
            $('#admin').click(function () {
                //判断是否是勾选状态
                var checked = $(this).prop('checked');
                if(checked){
                    //删除角色的div
                   roleDiv = $('#role').detach();
                } else {
                    $('#adminDiv').after(roleDiv);
                }
            });
            //获取复选框的状态
            var admin = $('#admin').prop('checked');
            if(admin) {
                //删除角色的div
                roleDiv = $('#role').detach();
            }
           /* //表当提交的时候为全选的状态
            $('#submitBtn').click(function () {
                //把右边 的下拉框的option 全部选择
                $('.selfRoles option').prop('selected',true);
                //提交表单
                $('#editForm').submit();
            });*/
            //已有的角色属性去重
            //获取右边属性和左边属性对比
            var ids = [];
            $('.selfRoles option').each(function (index, ele) {
                ids.push($(ele).val());//把每个角色的id 存放到数组中
            });
            //获取左边所有的角色
            $('.allRoles option').each(function (index, ele) {
                var id = $(ele).val();
                //判断当前的id是否存在
                if($.inArray(id,ids) != -1) {
                    //移除该数据
                    $(ele).remove();
                }
            });
            //表单使用验证插件
            $('#editForm').bootstrapValidator({
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields:{//配置不同字段的规则
                <#--<#if !employee??>-->
                    name:{
                        validators: {
                            notEmpty: {//不为空
                                message: ''//错误时的提示
                            },
                            stringLength: {//字符的长度
                                min: 2,
                                max: 10,
                                message: '请输入2-10的长度'
                            },
                            remote: {
                                type: 'POST',
                                url: '/employee/checkName.do',
                                message: '用户名已存在',
                                delay:1000,
                                data: function() {  //自定义提交参数，默认只会提交当前用户名input的参数
                                    return {
                                        id: $('[name="id"]').val(),
                                        name: $('[name="name"]').val()
                                    };
                                }
                            },
                        }
                    },
                <#--</#if>-->
                }
                /*email: {
                    validators: {
                        emailAddress: {
                            message: '请输入规范的邮箱'
                        },
                        notEmpty: {
                            message: ''
                        },
                    }
                },
                password: {
                    validators: {
                        notEmpty: {
                            message: ''
                        }
                    }
                },
                repassword: {
                    validators: {
                        notEmpty: {
                            message: ''
                        },
                        identical: {
                            field: 'password',
                            message: '请输入相同的密码'
                        },
                    }
                },
                age: {
                    validators: {
                        between: {
                            min: 18,
                            max: 100,
                            message: '禁止未成年的禁止使用'
                        }
                    }
                },
                dept: {
                    validators: {
                        notEmpty: {
                            message: ''
                        }
                    }
                },*/
            })
                    .on('success.form.bv', function(e) {
                        // 禁止之前的表单
                        e.preventDefault();
                        //设置右边的所有角色为选中的状态
                        $('.selfRoles option').prop('selected',true);
                        //使用jQuery-form 插件来提交异步的请求 里面封装了获取表单的数据
                        //一般如果页面已经有form 表单提交, 用插件提交比较方便
                        //如果没有表单 就用jQuery里面的get post  ajax的方法获取
                        $('#editForm').ajaxSubmit(function (data) {
                            if (data.success) {//登录成功
                                $.messager.popup(data.msg);
                                setTimeout(function () {
                                    window.location.href='/employee/list.do';
                                },2000);
                            } else {//登录失败
                                //alert(data.msg);
                                $.messager.popup(data.msg);
                            }
                        });
                    })
        });
    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <#include "../common/navbar.ftl">
    <!--菜单回显-->
    <#assign currentMenu="employee"/>
    <#include "../common/menu.ftl">
    <div class="content-wrapper">
        <section class="content-header">
            <h1>员工编辑</h1>
        </section>
        <section class="content">
            <div class="box">
                <form class="form-horizontal" action="/employee/saveOrUpdate.do" method="post" id="editForm">
                    <input type="hidden" value="${(employee.id)!}" name="id" >
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="name" class="col-sm-2 control-label">用户名：</label>
                        <div class="col-sm-6">
                            <input
                            <#--<#if employee??>
                                    readonly
                            </#if>-->
                                    type="text" class="form-control" id="name" name="name" value="${(employee.name)!}" placeholder="请输入用户名">
                        </div>
                    </div>
                    <#--区分有员工信息修改时不显示-->
                    <#if !employee??>
                    <div class="form-group">
                        <label for="password" class="col-sm-2 control-label">密码：</label>
                        <div class="col-sm-6">
                            <input type="password" class="form-control" id="password" name="password" placeholder="请输入密码">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="repassword" class="col-sm-2 control-label">验证密码：</label>
                        <div class="col-sm-6">
                            <input type="password" class="form-control" id="repassword" name="repassword" placeholder="再输入一遍密码">
                        </div>
                    </div>
                    </#if>
                    <div class="form-group">
                        <label for="email" class="col-sm-2 control-label">电子邮箱：</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="email" name="email" value="${(employee.email)!}" placeholder="请输入邮箱">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="age" class="col-sm-2 control-label">年龄：</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="age" name="age" value="${(employee.age)!}" placeholder="请输入年龄">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="dept" class="col-sm-2 control-label">部门：</label>
                        <div class="col-sm-6">
                            <select class="form-control" id="dept" name="dept.id">
                                //获取部门的回显参数
                                <#list departments as department>
                                //设置修改的部门回显
                                <option value="${(department.id)!}">${department.name}</option>
                                </#list>
                            </select>
                            <script>
                                $("#dept").val(${(employee.dept.id)!})
                            </script>
                        </div>
                    </div>
                    <div class="form-group" id="adminDiv">
                        <label for="admin" class="col-sm-2 control-label">超级管理员：</label>
                        <div class="col-sm-6"style="margin-left: 15px;">
                            <input type="checkbox" id="admin" name="admin" class="checkbox">
                            <#if employee?? && employee.admin>
                                <script>
                                    $("#admin").prop("checked", true);
                                </script>
                            </#if>
                        </div>
                    </div>
                    <div class="form-group " id="role">
                            <label for="role" class="col-sm-2 control-label">分配角色：</label><br/>
                            <div class="row" style="margin-top: 10px">
                                <div class="col-sm-2 col-sm-offset-2">
                                    <select multiple class="form-control allRoles" size="15">
                                        <#list roles as roles>
                                            <option value="${roles.id}">${roles.name}</option>
                                        </#list>
                                    </select>
                                </div>
                            <div class="col-sm-1" style="margin-top: 60px;" align="center">
                                <div>

                                    <a type="button" class="btn btn-primary  " style="margin-top: 10px" title="右移动"
                                       onclick="moveSelected('allRoles', 'selfRoles')">
                                        <span class="glyphicon glyphicon-menu-right"></span>
                                    </a>
                                </div>
                                <div>
                                    <a type="button" class="btn btn-primary " style="margin-top: 10px" title="左移动"
                                       onclick="moveSelected('selfRoles', 'allRoles')">
                                        <span class="glyphicon glyphicon-menu-left"></span>
                                    </a>
                                </div>
                                <div>
                                    <a type="button" class="btn btn-primary " style="margin-top: 10px" title="全右移动"
                                       onclick="moveAll('allRoles', 'selfRoles')">
                                        <span class="glyphicon glyphicon-forward"></span>
                                    </a>
                                </div>
                                <div>
                                    <a type="button" class="btn btn-primary " style="margin-top: 10px" title="全左移动"
                                       onclick="moveAll('selfRoles', 'allRoles')">
                                        <span class="glyphicon glyphicon-backward"></span>
                                    </a>
                                </div>
                            </div>

                            <div class="col-sm-2">
                                <select multiple class="form-control selfRoles" size="15" name="ids">
                                    <#list (employee.roles)! as roles>
                                        <option value="${roles.id}">${roles.name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-offset-1 col-sm-6">
                            <button id="submitBtn" type="submit" class="btn btn-primary submitBtn">保存</button>
                            <button type="reset" class="btn btn-danger">重置</button>
                        </div>
                    </div>

                </form>

            </div>
        </section>
    </div>
    <#include "../common/footer.ftl">
</div>
</body>
</html>
