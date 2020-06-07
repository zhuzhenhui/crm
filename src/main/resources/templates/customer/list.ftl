<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>客户列表</title>
<#--相对的路径-->
    <#include "../common/link.ftl">
    <script>
        $(function () {
            //新增和编辑
            $('.btn-input').click(function () {
                //清楚模态框
                $('#editForm input,#editForm select').val('');
                //获取当前按钮的属性值
                //使用data方法,可以把json字符串转为js对象, data方法中传的值,就是data-后面的字符串
                var json = $(this).data("json");
                if (json) { //json有数据代表是编辑
                    $("#editForm input[name=id]").val(json.id);
                    $("#editForm input[name=name]").val(json.name);
                    $("#editForm input[name=age]").val(json.age);
                    $("#editForm input[name=qq]").val(json.qq);
                    $("#editForm select[name=gender]").val(json.gender);
                    $("#editForm input[name=tel]").val(json.tel);
                    $("#editForm select[name='job.id']").val(json.jobId);
                    $("#editForm select[name='source.id']").val(json.sourceId);
                }
                //弹出模态框
                $('#editModal').modal('show');
            });
            //修改客户的状态
            $('.btn-date').click(function () {

                //针对只读提交的数据
                //数据复原
                $("#editDate input").val("");
                //$("#editDate")[0].reset();
                //获取当前按钮的属性值
                //使用data方法,可以把json字符串转为js对象, data方法中传的值,就是data-后面的字符串
                var json = $(this).data("json");
                if (json) { //json有数据代表是编辑
                    $("#editDate input[name=customerId]").val(json.id);
                    $("#editDate input[name=name]").val(json.name);
                }
                //弹出模态框
                $('#statusEditDate').modal('show');
            });
            //提交文件上传表单
            $(".submitBtns").click(function () {
                $("#editDate").ajaxSubmit(handlerMessage);
            });
            //删除的按钮操作
            $('.btn-delete').click(function () {
                //获取客户的id值
                var id = $(this).data("id");
                console.log(id);
                //确认框
                $.messager.confirm('警告', '是否要删除该数据?', function () { //点击确定后的回调函数
                    $.post('/customer/delete.do', {"id": id}, handlerMessage);
                });
            });

            //表单使用验证插件
            $('#editForm').bootstrapValidator({
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {//配置不同字段的规则
                <#--<#if !employee??>-->
                    tel: {
                        validators: {
                            notEmpty: {//不为空
                                message: ''//错误时的提示
                            },
                            stringLength: {//字符的长度
                                min: 3,
                                max: 11,
                                message: '请输入正确的手机号'
                            },
                            remote: {
                                type: 'POST',
                                url: '/customer/checkName.do',
                                message: '手机号已经存在',
                                delay: 1000,
                                data: function () {  //自定义提交参数，默认只会提交当前用户名input的参数
                                    return {
                                        id: $('[name="id"]').val(),
                                        name: $('[name="tel"]').val()
                                    };
                                }
                            },
                        }
                    },
                    age: {
                        validators: {
                            between: {
                                min: 18,
                                max: 100,
                                message: '禁止未成年使用'
                            }
                        }
                    }
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
                age: {
                    validators: {
                        between: {
                            min: 18,
                            max: 100,
                            message: '禁止未成年的禁止使用'
                        }
                    }
                },*/
            })
            .on('success.form.bv', function (e) {
                // 禁止之前的表单
                e.preventDefault();
                //使用jQuery-form 插件来提交异步的请求 里面封装了获取表单的数据
                //一般如果页面已经有form 表单提交, 用插件提交比较方便
                //如果没有表单 就用jQuery里面的get post  ajax的方法获取
                $('#editForm').ajaxSubmit(function (data) {
                    if (data.success) {//登录成功
                        $.messager.popup(data.msg);
                        setTimeout(function () {
                            window.location.href = '/customer/potentialList.do';
                        }, 2000);
                    } else {//登录失败
                        //alert(data.msg);
                        $.messager.popup(data.msg);
                    }
                });
            });
            //更进的操作
            $(".btn-trace").click(function () {
                //客户对象
                var json = $(this).data('json');
                if (json) { //json有数据代表是编辑
                    $("#traceForm input[name='customer.id']").val(json.id);
                    $("#traceForm input[name='customer.name']").val(json.name);
                }

                //模态框的操作
                $('#traceModal').modal("show");

            });
            $('.trace-submit').click(function () {
                $('#traceForm').ajaxSubmit(handlerMessage);
            });
            //移交操作
            $(".btn-transfer").click(function () {
                //客户对象
                var json = $(this).data('json');
                if (json) { //json有数据代表是编辑
                    $("#transferForm input[name='customer.name']").val(json.name);
                    $("#transferForm input[name='customer.id']").val(json.id);
                    $("#transferForm input[name='oldSeller.name']").val(json.sellerName);
                    $("#transferForm input[name='oldSeller.id']").val(json.sellerId);
                }
                //模态框的操作
                $('#transferModal').modal("show");
            });
            $('.transfer-submit').click(function () {
                $('#transferForm').ajaxSubmit(handlerMessage);
            });
        });
    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <#include "../common/navbar.ftl">
    <!--菜单回显-->
    <#assign currentMenu="customer"/>
    <#include "../common/menu.ftl">
    <div class="content-wrapper">
        <section class="content-header">
            <h1>客户列表</h1>
        </section>
        <section class="content">
            <div class="box">
                <!--高级查询--->
                <form class="form-inline" id="searchForm" action="/customer/list.do" method="post">
                    <input type="hidden" name="currentPage" id="currentPage" value="1">
                    <div class="form-group">
                        <label for="keyword">关键字:</label>
                        <input type="text" class="form-control" id="keyword" name="keyword" value="${(qo.keyword)!}"
                               placeholder="请输入姓名/电话">
                    </div>
                    <#--针对管理员和经理的显示查找-->
                    <@shiro.hasAnyRoles name="Admin,Market_Manager">
                    <div class="form-group">
                        <label for="seller"> 部门:</label>
                        <select class="form-control" id="seller" name="sellerId">
                            <option value="-1">全部</option>
                                <#list sellers! as s>
                                    <option value="${s.id!}">${s.name!}</option>
                                </#list>
                        </select>
                        <script>
                            $("#seller option[value='${(qo.sellerId)!}']").prop("selected", true);
                        </script>
                    </div>
                    </@shiro.hasAnyRoles>
                    <button id="btn_query" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> 查询
                    </button>
                    <a href="#" class="btn btn-success btn-input" style="margin: 10px">
                        <span class="glyphicon glyphicon-plus"></span> 添加
                    </a>
                </form>
                <!--编写内容-->
                <div class="box-body table-responsive no-padding ">
                    <table class="table table-hover table-bordered">
                        <tr>
                            <th>编号</th>
                            <th>姓名</th>
                            <th>年龄</th>
                            <th>电话</th>
                            <th>QQ</th>
                            <th>职业</th>
                            <th>来源</th>
                            <th>销售员</th>
                            <th>状态</th>
                            <th>录入时间</th>
                            <th>操作</th>
                        </tr>
                        <#list pageInfo.list as customer>
                            <tr>
                                <td>${customer_index+1}</td>
                                <td>${customer.name!}</td>
                                <td>${customer.age!}</td>
                                <td>${customer.tel!}</td>
                                <td>${customer.qq!}</td>
                                <td>${(customer.job.title)!}</td>
                                <td>${(customer.source.title)!}</td>
                                <td>${(customer.seller.name)!}</td>
                                <td>${customer.statusName}</td>
                                <td>${(customer.inputTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                                <td>
                                    <@shiro.hasPermission name="customer:saveOrUpdate">
                                    <a href="#" class="btn btn-info btn-xs btn-input" data-json='${customer.json!}'>
                                        <span class="glyphicon glyphicon-pencil"></span> 编辑
                                    </a>
                                    </@shiro.hasPermission>
                                    <a href="#" class="btn btn-danger btn-xs btn-trace" data-json='${customer.json!}'>
                                        <span class="glyphicon glyphicon-phone"></span> 跟进
                                    </a>
                                    <!--管理员和经理才能看到该下拉框-->
                                    <@shiro.hasAnyRoles name="Admin,Market_Manager" >
                                        <a href="#"
                                                      class="btn btn-danger btn-xs btn-transfer" data-json='${customer.json!}'>
                                        <span class="glyphicon glyphicon-phone"></span> 移交
                                    </a>
                                    </@shiro.hasAnyRoles>
                                    <a href="#" class="btn btn-warning btn-xs btn-date" data-json='${customer.json!}'>
                                        <span class="glyphicon glyphicon-repeat"></span> 修改状态
                                    </a>
                                    <@shiro.hasPermission name="customer:delete">
                                    <a href="#" class="btn btn-danger btn-xs btn-delete" data-id='${customer.id}'>
                                        <span class="glyphicon glyphicon-trash"></span> 删除
                                    </a>
                                    </@shiro.hasPermission>
                                </td>
                            </tr>
                        </#list>
                    </table>
                    <!--分页-->
                    <#include "../common/page.ftl">
                </div>
            </div>
        </section>
    </div>
    <#include "../common/footer.ftl">
</div>

<!-- 添加或者修改Modal -->
<div class="modal fade" id="editModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title inputTitle">客户编辑</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" action="/customer/saveOrUpdate.do" method="post" id="editForm">
                    <input type="hidden" value="" name="id">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">客户名称：</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" name="name"
                                   placeholder="请输入客户姓名"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">客户年龄：</label>
                        <div class="col-sm-6">
                            <input type="number" class="form-control" name="age"
                                   placeholder="请输入客户年龄"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">客户性别：</label>
                        <div class="col-sm-6">
                            <select class="form-control" name="gender">
                                <option value="1">男</option>
                                <option value="0">女</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">客户电话：</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" name="tel"
                                   placeholder="请输入客户电话"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">客户QQ：</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" name="qq"
                                   placeholder="请输入客户QQ"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">客户工作：</label>
                        <div class="col-sm-6">
                            <select class="form-control" name="job.id">
                                <#list jobs as j>
                                    <option value="${j.id}">${j.title}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">客户来源：</label>
                        <div class="col-sm-6">
                            <select class="form-control" name="source.id">
                                <#list sources as s>
                                    <option value="${s.id}">${s.title}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
            </div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-default submitBtn">保存</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal">取消</button>
            </div>
            </form>
        </div>
    </div>
</div>

<!-- 修改状态Modal -->
<div class="modal fade" id="statusEditDate" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">修改客户状态</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" action="/customer/statusOrUpdate.do" method="post" id="editDate">
                    <input type="hidden" name="customerId" id="customerId">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">客户名称：</label>
                        <div class="col-sm-6">
                        <#--后端传过去的数据-->
                        <#--给用户看的-->
                            <input readonly class="form-control" name="name">
                        </div>
                    </div>
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="status" class="col-sm-3 control-label">客户状态：</label>
                        <div class="col-sm-6">
                            <select class="form-control" id="status" name="status">
                                <option value="-1">全部</option>
                                <#list status! as s>
                                    <option value="${s.status!}">${s.statusName!}</option>
                                </#list>
                            </select>
                            <script>
                                $("#status option[value='${(qo.status)!}']").prop("selected", true);
                            </script>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default submitBtns">保存</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal">取消</button>
            </div>

        </div>
    </div>
</div>
<#--跟进的模态框-->
<div class="modal fade" id="traceModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">跟进</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" action="/customerTraceHistory/saveOrUpdate.do" method="post" id="traceForm">
                    <div class="form-group" >
                        <label class="col-lg-4 control-label">客户姓名：</label>
                        <div class="col-lg-6">
                            <#--传过去相应的参数-->
                            <input type="hidden" name="customer.id">
                            <#--给客户看的-->
                            <input type="text" readonly name="customer.name" class="form-control" >
                        </div>
                    </div>
                    <div class="form-group" >
                        <label class="col-lg-4 control-label">跟进时间：</label>
                        <div class="col-lg-6 ">
                            <input type="text" class="form-control"  name="traceTime"  placeholder="请输入跟进时间">
                            <script>
                                //跟进时间使用日期插件
                                $("input[name=traceTime]").datepicker({
                                    clearBtn: true,
                                    language: "zh-CN", //语言
                                    autoclose: true, //选择日期后自动关闭
                                    todayHighlight: true //高亮今日日期
                                });
                            </script>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-lg-4 control-label">交流方式：</label>
                        <div class="col-lg-6">
                            <select class="form-control" name="traceType.id">
                                <#list ccts as c>
                                    <option value="${c.id}">${c.title}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-4 control-label">跟进结果：</label>
                        <div class="col-lg-6">
                            <select class="form-control" name="traceResult">
                                <option value="3">优</option>
                                <option value="2">中</option>
                                <option value="1">差</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group" >
                        <label class="col-lg-4 control-label">跟进记录：</label>
                        <div class="col-lg-6">
                            <textarea type="text" class="form-control" name="traceDetails"
                                      placeholder="请输入跟进记录" name="remark"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary trace-submit">保存</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<#--移交的模态框-->
<div id="transferModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">移交</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" action="/customerTransfer/saveOrUpdate.do" method="post" id="transferForm" style="margin: -3px 118px">
                    <div class="form-group" >
                        <label for="name" class="col-sm-4 control-label">客户姓名：</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control"  name="customer.name"   readonly >
                            <input type="hidden" class="form-control"  name="customer.id"  >
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="sn" class="col-sm-4 control-label">旧营销人员：</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control"  name="oldSeller.name" readonly >
                            <input type="hidden" class="form-control"  name="oldSeller.id"  >
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="sn" class="col-sm-4 control-label">新营销人员：</label>
                        <div class="col-sm-8">
                            <select name="newSeller.id" class="form-control">
                                <#list sellers as e>
                                    <option value="${e.id}">${e.name}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="sn" class="col-sm-4 control-label">移交原因：</label>
                        <div class="col-sm-8">
                            <textarea type="text" class="form-control" id="reason" name="reason" cols="10" ></textarea>
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary transfer-submit">保存</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
