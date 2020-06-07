<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>员工管理</title>
    <#include "../common/link.ftl" >
    <script>
        $(function () {
            //全选/反选
            $('#allck').click(function () {
                //把当前全选的复选框的checked状态, 设置到列表厘面的所有复选框中
                var checked = $(this).prop('checked');
                $('.cb').prop('checked',checked);
            });
            //列表中的复选框
            $('.cb').click(function () {
                //判断列表中是否全部勾选, 如果是 也需要设置allck的勾选
                $('#allck').prop('checked',$('.cb:checked').length==$('.cb').length);
            });
            //删除的按钮操作
            $('.btn_delete').click(function () {
                //获取部门的id值
                var id = $(this).data("id");
                console.log(id);
                //确认框
                $.messager.confirm('警告','是否要删除该数据?',function(){ //点击确定后的回调函数
                    $.post('/employee/delete.do',{"id":id},handlerMessage);
                });
            });
            //批量删除
            $(".btn_batchDelete").click(function () {
                //判断是否已经选中了数据
                var cbs = $('.cb:checked');
                //如果没有选中就需要提示
                if(cbs.length==0) {
                    $.messager.popup("您,没有选中数据!");
                    return;
                }
                //提示确认框
                $.messager.confirm('警告','您是否要批量删除该数据?',function(){ //点击确定后的回调函数
                    //获取选中的数据, 发ajax 请求提交到后台 没有from表单
                    var ids = [];
                    cbs.each(function (index, ele) {
                        var id = $(ele).data('id');
                        ids.push(id);
                    });
                    $.post('/employee/batchDelete.do',{ids:ids},handlerMessage);
                });
            });

            //导入事件
            $('.btn-import').click(function () {
                $("#importModal").modal('show');
            });
            //提交文件上传表单
            $(".btn-submit").click(function () {
                $("#importForm").ajaxSubmit(handlerMessage)
            });
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
            <h1>员工管理</h1>
        </section>
        <section class="content">
            <div class="box">
                <!--高级查询--->
                <div style="margin: 10px;">
                    <form class="form-inline" id="searchForm" action="/employee/list.do" method="post">
                        <input type="hidden" name="currentPage" id="currentPage" value="1">
                        <div class="form-group">
                            <label for="keyword">关键字:</label>
                            <input type="text" class="form-control" id="keyword" name="keyword" value="${(qo.keyword)!}" placeholder="请输入姓名/邮箱">
                        </div>
                        <div class="form-group">
                            <label for="dept"> 部门:</label>
                            <select class="form-control" id="dept" name="deptId">
                                <option value="-1">全部</option>
                                <#list departments as d>
                                    <option value="${d.id}">${d.name}</option>
                                </#list>
                            </select>
                            <script>
                                $("#dept option[value='${(qo.deptId)!}']").prop("selected", true);
                            </script>
                        </div>
                        <button id="btn_query" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> 查询</button>
                        <a href="/employee/input.do" class="btn btn-success btn_redirect">
                            <span class="glyphicon glyphicon-plus"></span> 添加
                        </a>
                        <a href="#" class="btn btn-danger btn_batchDelete">
                            <span class="glyphicon glyphicon-trash"></span> 批量删除
                        </a>
                        <a href="/employee/exportXls.do" class="btn btn-warning" >
                            <span class="glyphicon glyphicon-download"></span> 导出
                        </a>
                        <a href="#" class="btn btn-warning btn-import">
                            <span class="glyphicon glyphicon-upload"></span> 导入
                        </a>
                    </form>
                </div>
                <table class="table table-hover table-bordered">
                    <thead>
                    <tr>
                        <th><input type="checkbox" id = "allck"></th>
                        <th>编号</th>
                        <th>名称</th>
                        <th>email</th>
                        <th>年龄</th>
                        <th>部门</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <#list pageInfo.list as employee>
                        <tr>
                            <td><input type="checkbox" class="cb" data-id="${employee.id}"></td>
                            <td>${employee_index + 1}</td>
                            <td>${employee.name!}</td>
                            <td>${employee.email!}</td>
                            <td>${(employee.age)!}</td>
                            <td>${(employee.dept.name)!}</td>
                            <td>
                                <a href="/employee/input.do?id=${employee.id}" class="btn btn-info btn-xs btn_redirect">
                                    <span class="glyphicon glyphicon-pencil"></span> 编辑
                                </a>
                                <a href="#" class="btn btn-danger btn-xs btn_delete" data-id='${employee.id}'>
                                    <span class="glyphicon glyphicon-trash"></span> 删除
                                </a>
                                <a href="/employee/resetPwd.do?id=${employee.id}" class="btn btn-warning btn-xs btn_delete">
                                    <span class="glyphicon glyphicon-repeat"></span> 重置秘密
                                </a>
                            </td>
                        </tr>
                    </#list>
                </table>
                <!--分页-->
                <#include "../common/page.ftl">
            </div>
        </section>
    </div>
    <#include "../common/footer.ftl">
</div>
<div class="modal fade" id="importModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">导入</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" action="/employee/importXls.do" method="post" id="importForm">
                    <input type="hidden" name="id">
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="name" class="col-sm-3 control-label"></label>
                        <div class="col-sm-6">
                            <input type="file" name="file">
                        </div>
                    </div>
                    <div class="form-group" style="margin-top: 10px;">
                        <div class="col-sm-3"></div>
                        <div class="col-sm-6">
                            <a href="/xlstemplates/employee_import.xls" class="btn btn-success" >
                                <span class="glyphicon glyphicon-download"></span> 下载模板
                            </a>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary btn-submit">保存</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
