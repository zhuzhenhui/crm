<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>订单管理</title>
    <#--相对的路径-->
    <#include "../common/link.ftl">
    <script>
        $(function () {
            //新增
            $('.btn-input').click(function () {
                //清楚模态框
                $('input,select').val('');
                //获取当前按钮的属性值
                //设置目录的显示为只读
                //使用data方法,可以把json字符串转为js对象, data方法中传的值,就是data-后面的字

                //弹出模态框
                $('#ediModal').modal('show');
            });
            $('.submitBtn').click(function () {//提交按钮
                //使用jQuery-form 插件来提交异步的请求 里面封装了获取表单的数据
                //一般如果页面已经有form 表单提交, 用插件提交比较方便
                //如果没有表单 就用jQuery里面的get post  ajax的方法获取
                $('#editForm').ajaxSubmit(handlerMessage);
            });
            //编辑
            $('.btn-inputs').click(function () {
                //清楚模态框
                //获取当前按钮的属性值
                //设置目录的显示为只读
                //使用data方法,可以把json字符串转为js对象, data方法中传的值,就是data-后面的字符串
                var json = $(this).data("json");
                if(json) {
                    //会显数据
                    $("input[name=id]").val(json.id);
                    $("select[name='customer.id']").val(json.customerId);
                    $("select[name='course.id']").val(json.courseId);
                    $("input[name=money]").val(json.money);
                }
                //弹出模态框
                $('#ediModals').modal('show');
            });
            $('.submitBtns').click(function () {//提交按钮
                //使用jQuery-form 插件来提交异步的请求 里面封装了获取表单的数据
                //一般如果页面已经有form 表单提交, 用插件提交比较方便
                //如果没有表单 就用jQuery里面的get post  ajax的方法获取
               $('#editForms').ajaxSubmit(handlerMessage);
            });
               //删除的按钮操作
            $('.btn-delete').click(function () {
                //获取订单的id值
                var id = $(this).data("id");
                console.log(id);
                //确认框
                $.messager.confirm('警告','是否要删除该数据?',function(){ //点击确定后的回调函数
                    $.post('/courseorder/delete.do',{"id":id},handlerMessage);
                });
            });
        });
    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <#include "../common/navbar.ftl">
    <!--菜单回显-->
    <#assign currentMenu="courseorder"/>
    <#include "../common/menu.ftl">
    <div class="content-wrapper">
        <section class="content-header">
            <h1>订单管理</h1>
        </section>
        <section class="content">
            <div class="box">
                <!--高级查询--->
                <form class="form-inline" id="searchForm" action="/courseorder/list.do" method="post">
                    <input type="hidden" name="currentPage" id="currentPage" value="1">
                    <div class="form-group">
                        <label for="keyword">关键字:</label>
                        <input type="text" class="form-control" id="keyword" name="keyword" value="${(qo.keyword)!}" placeholder="请输入姓名">
                    </div>
                    <div class="form-group">
                        <label for="course"> 销售课程:</label>
                        <select class="form-control" id="course" name="courseId">
                            <option value="-1">全部</option>
                                <#list subjects! as s>
                                    <option value="${s.id!}">${s.title!}</option>
                                </#list>
                        </select>
                        <script>
                            $("#course option[value='${(qo.courseId)!}']").prop("selected", true);
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
                            <th>客户名称</th>
                            <th>销售课程</th>
                            <th>销售时间</th>
                            <th>销售金额</th>
                            <th>操作</th>
                        </tr>
                        <#list pageInfo.list as courseorder>
                            <tr>
                                <td>${courseorder_index+1}</td>
                                <td>${(courseorder.customer.name)!}</td>
                                <td>${(courseorder.course.title)!}</td>
                                <td>${(courseorder.inputTime?string("yyyy-MM-dd"))!}</td>
                                <td>${(courseorder.money)!}</td>
                                <td>
                                    <@shiro.hasPermission name="courseorder:saveOrUpdate">
                                    <a href="#" class="btn btn-info btn-xs btn-inputs" data-json='${courseorder.json!}'>
                                        <span class="glyphicon glyphicon-pencil"></span> 编辑
                                    </a>
                                    </@shiro.hasPermission>
                                    <@shiro.hasPermission name="courseorder:delete">
                                    <a href="#" class="btn btn-danger btn-xs btn-delete"  data-id='${courseorder.id!}'>
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

<!-- 添加Modal -->
<div class="modal fade" id="ediModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">添加/修改</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" action="/courseorder/saveOrUpdate.do" method="post" id="editForm">
                    <input type="hidden" value="${(courseorder.id)!}" name="id">
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="customer" class="col-sm-3 control-label">选择客户：</label>
                        <div class="col-sm-6">
                                <select class="form-control" id="customerId" name="customer.id">
                                    <option value="-1">全部</option>
                                        <#list customers! as s>
                                            <option value="${s.id!}">${s.name!}</option>
                                        </#list>
                                </select>
                        </div>
                    </div>
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="course" class="col-sm-3 control-label">选择课程：</label>
                        <div class="col-sm-6">
                        <select class="form-control" id="courseId" name="course.id">
                            <option value="-1">全部</option>
                                <#list subjects! as s>
                                    <option value="${s.id!}">${s.title!}</option>
                                </#list>
                        </select>
                        </div>
                    </div>
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="money" class="col-sm-3 control-label">课程金额：</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="money" name="money"
                                   placeholder="请输入课程金额">
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

!-- 编辑Modal -->
<div class="modal fade" id="ediModals" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">添加/修改</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" action="/courseorder/saveOrUpdate.do" method="post" id="editForms">
                    <input type="hidden" value="${(courseorder.id)!}" name="id">
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="customer" class="col-sm-3 control-label">选择客户：</label>
                        <div class="col-sm-6">
                            <select class="form-control" id="customerId" name="customer.id">
                                <option value="-1">全部</option>
                                        <#list customer! as s>
                                            <option value="${s.id!}">${s.name!}</option>
                                        </#list>
                            </select>
                        </div>
                    </div>
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="course" class="col-sm-3 control-label">选择课程：</label>
                        <div class="col-sm-6">
                            <select class="form-control" id="courseId" name="course.id">
                                <option value="-1">全部</option>
                                <#list subjects! as s>
                                    <option value="${s.id!}">${s.title!}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="money" class="col-sm-3 control-label">课程金额：</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="money" name="money"
                                   >
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
</body>
</html>
