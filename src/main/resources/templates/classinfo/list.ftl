<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>班级管理</title>
    <#--相对的路径-->
    <#include "../common/link.ftl">
    <script>
        $(function () {
            //新增和编辑
            $('.btn-input').click(function () {
                //清楚模态框
                $('input').val('');
                //获取当前按钮的属性值
                //使用data方法,可以把json字符串转为js对象, data方法中传的值,就是data-后面的字符串
                var json = $(this).data("json");
                console.log(json);
                if(json) {
                    //回显数据
                    $("input[name=id]").val(json.id);
                    $("input[name=name]").val(json.name);
                    $("input[name=number]").val(json.number);
                    $("#teachers").val(json.teacher.id);
                }
                //弹出模态框
                $('#ediModal').modal('show');
            });
            $('.submitBtn').click(function () {//提交按钮
                //获取input里面输入的内容
                //var username = $("input[name=username]").val();
                //var password = $("input[name=password]").val();
                //获取表单中的所有input/select等表单控件的数据
                /*var params = $('#editForm').serialize();
                //提交异步请求
                $.post("/classinfo/saveOrUpdate.do",params,function (data) {
                    if (data.success) {//登录成功
                        $.messager.popup(data.msg);
                        setTimeout(function () {
                            window.location.reload();
                        },2000);
                    } else {//登录失败
                        //alert(data.msg);
                        $.messager.popup(data.msg);
                    }
                });*/
                //使用jQuery-form 插件来提交异步的请求 里面封装了获取表单的数据
                //一般如果页面已经有form 表单提交, 用插件提交比较方便
                //如果没有表单 就用jQuery里面的get post  ajax的方法获取
               $('#editForm').ajaxSubmit(handlerMessage);
            });
               //删除的按钮操作
            $('.btn-delete').click(function () {
                //获取班级的id值
                var id = $(this).data("id");
                console.log(id);
                //确认框
                $.messager.confirm('警告','是否要删除该数据?',function(){ //点击确定后的回调函数
                    $.post('/classinfo/delete.do',{"id":id},handlerMessage);
                });
            });
        });
    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <#include "../common/navbar.ftl">
    <!--菜单回显-->
    <#assign currentMenu="classinfo"/>
    <#include "../common/menu.ftl">
    <div class="content-wrapper">
        <section class="content-header">
            <h1>班级管理</h1>
        </section>
        <section class="content">
            <div class="box">
                <!--高级查询--->
                <form class="form-inline" id="searchForm" action="/classinfo/list.do" method="post">
                    <input type="hidden" name="currentPage" id="currentPage" value="1">
                    <div class="form-group">
                        <label for="keyword">关键字:</label>
                        <input type="text" class="form-control" id="keyword" name="keyword" value="${(qo.keyword)!}" placeholder="请输入姓名/邮箱">
                    </div>
                    <div class="form-group">
                        <label for="teacher"> 班主任:</label>
                        <select class="form-control" id="teacher" name="teacherId">
                            <option value="-1">全部</option>
                                <#list teachers! as t>
                                    <option value="${t.id}">${t.name}</option>
                                </#list>
                        </select>
                        <script>
                            $("#teacher option[value='${(qo.teacherId)!}']").prop("selected", true);
                        </script>
                    </div>
                    <button id="btn_query" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> 查询</button>
                    <a href="#" class="btn btn-success btn-input" style="margin: 10px">
                        <span class="glyphicon glyphicon-plus"></span> 添加
                    </a>
                </form>
                <!--编写内容-->
                <div class="box-body table-responsive no-padding ">
                    <table class="table table-hover table-bordered">
                        <tr>
                            <th>编号</th>
                            <th>班级名称</th>
                            <th>班级人数</th>
                            <th>班级主任</th>
                            <th>操作</th>
                        </tr>
                        <#list pageInfo.list as classinfo>
                            <tr>
                                <td>${classinfo_index+1}</td>
                                <td>${(classinfo.name)!}</td>
                                <td>${(classinfo.number)!}</td>
                                <td>${(classinfo.teacher.name)!}</td>
                                <td>
                                    <a href="#" class="btn btn-info btn-xs btn-input" data-json='${(classinfo.json)!}'>
                                        <span class="glyphicon glyphicon-pencil"></span> 编辑
                                    </a>
                                    <a href="#" class="btn btn-danger btn-xs btn-delete"  data-id='${classinfo.id}'>
                                        <span class="glyphicon glyphicon-trash"></span> 删除
                                    </a>
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
<div class="modal fade" id="ediModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">添加/修改</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" action="/classinfo/saveOrUpdate.do" method="post" id="editForm">
                    <input type="hidden" value="${(classinfo.id)!}" name="id">
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="name" class="col-sm-3 control-label">班级名称：</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="name" name="name"
                                   placeholder="请输入班级名">
                        </div>
                    </div>
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="number" class="col-sm-3 control-label">班级人数：</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="number" name="number"
                                   placeholder="请输入班级人数">
                        </div>
                    </div>
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="teachers" class="col-sm-3 control-label">班主任：</label>
                        <div class="col-sm-6">
                            <select class="form-control" id="teachers" name="teacher.id">
                                <#list teachers! as t>
                                    <option value="${t.id}">${t.name}</option>
                                </#list>
                            </select>
                            <#--
                            $("#teachers option[value='${(qo.teacherId)!}']").prop("selected", true);
                            $("#teacher").val(${(json.teacher.id)!});
                            <input type="text" class="form-control" id="teacher_id" name="teacher_id"
                                   placeholder="请输入班主任姓名">-->
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default submitBtn">保存</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
