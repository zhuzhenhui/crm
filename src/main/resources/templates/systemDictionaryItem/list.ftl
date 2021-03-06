﻿<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>字典管理</title>
    <#--相对的路径-->
    <#include "../common/link.ftl">
    <script>
        $(function () {
            $('.btn-inputs').click(function () {
                //没选中的提示
                $.messager.popup("请选择左边目录,进行操作!!!");
            });

            //新增和编辑
            $('.btn-input').click(function () {

                //清楚模态框
                $('input').val('');
                //设置目录的显示为只读
                $("input[name=parentName]").val($('#${qo.parentId}').html());
                //针对只读提交的数据
                $("input[name=parentId]").val("${qo.parentId}");
                //获取当前按钮的属性值
                //使用data方法,可以把json字符串转为js对象, data方法中传的值,就是data-后面的字符串
                var json = $(this).data("json");
                if(json) {
                    //会显数据
                    $("input[name=id]").val(json.id);
                    $("input[name=title]").val(json.title);
                    $("input[name=sequence]").val(json.sequence);
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
                $.post("/systemDictionaryItem/saveOrUpdate.do",params,function (data) {
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
                //获取字典的id值
                var id = $(this).data("id");
                console.log(id);
                //确认框
                $.messager.confirm('警告','是否要删除该数据?',function(){ //点击确定后的回调函数
                    $.post('/systemDictionaryItem/delete.do',{"id":id},handlerMessage);
                });
            });
        });
    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <#include "../common/navbar.ftl">
    <!--菜单回显-->
    <#assign currentMenu="systemDictionaryItem"/>
    <#include "../common/menu.ftl">
    <div class="content-wrapper">
        <section class="content-header">
            <h1>字典管理</h1>
        </section>
        <section class="content">
            <div class="box">
                <div class="row">
                    <div class="col-md-3">
                        <div class="panel panel-default">
                            <div class="panel-heading" style="text-align: center">字典目录</div>
                            <div class="panel-body">
                                <div class="list-group">
                                    <#list systemDictionarys! as s>
                                        <a id= "${s.id}" href="/systemDictionaryItem/list.do?parentId=${s.id}"
                                           class="list-group-item" style="text-align: center">${s.title}
                                        </a>
                                    </#list>
                                    <script>
                                        <#--获取指定的a 连接,添加active 样式-->
                                        $('#${qo.parentId}').addClass('active');
                                    </script>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-9">
                        <!--高级查询--->
                        <form class="form-inline" id="searchForm" action="/systemDictionaryItem/list.do" method="post">
                            <input type="hidden" name="currentPage" id="currentPage" value="1">
                            <#--分页时会丢失数据, 补上-->
                            <input type="hidden" name="parentId"  value="${qo.parentId}">
                                <#if qo.parentId==-1>

                                    <a href="#" class="btn btn-success btn-inputs" style="margin: 10px">
                                        <span class="glyphicon glyphicon-plus"></span> 添加
                                    </a>

                                <#else>
                                    <a href="#" class="btn btn-success btn-input" style="margin: 10px">
                                    <span class="glyphicon glyphicon-plus"></span> 添加
                                    </a>
                                </#if>
                        </form>
                        <#if qo.parentId==-1>
                                <h3 style="text-align: center">欢迎使用字典目录!</h3>
                        <#else>
                        <!--编写内容-->
                        <div class="box-body table-responsive no-padding ">
                            <table class="table table-hover table-bordered">
                                <tr>
                                    <th>编号</th>
                                    <th>标题</th>
                                    <th>序号</th>
                                    <th>操作</th>
                                </tr>
                        <#list pageInfo.list as systemDictionaryItem>
                            <tr>
                                <td>${systemDictionaryItem_index+1}</td>
                                <td>${systemDictionaryItem.title}</td>
                                <td>${(systemDictionaryItem.sequence)!"${(systemDictionaryItem.sequence)!}"}</td>
                                <td>
                                    <@shiro.hasPermission name="systemDictionaryItem:saveOrUpdate">
                                    <a href="#" class="btn btn-info btn-xs btn-input" data-json='${systemDictionaryItem.json}'>
                                        <span class="glyphicon glyphicon-pencil"></span> 编辑
                                    </a>
                                    </@shiro.hasPermission>
                                    <@shiro.hasPermission name="systemDictionaryItem:delete">
                                    <a href="#" class="btn btn-danger btn-xs btn-delete"  data-id='${systemDictionaryItem.id}'>
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
                </div>

            </div>
        </section>
    </div>
    <#include "../common/footer.ftl">
</div>
</#if>
<!-- 添加或者修改Modal -->
<div class="modal fade" id="ediModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">添加/修改</h4>
                </div>
            <div class="modal-body">
                <form class="form-horizontal" action="/systemDictionaryItem/saveOrUpdate.do" method="post" id="editForm">
                    <input type="hidden" value="${(systemDictionaryItem.id)!}" name="id">
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="parentId" class="col-sm-3 control-label">目录：</label>
                        <div class="col-sm-6">
                                <#--后端传过去的数据-->
                                <input type="hidden" id="parentId" name="parentId">
                                <#--给用户看的-->
                                <input  readonly class="form-control" name="parentName">
                        </div>
                    </div>
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="title" class="col-sm-3 control-label">标题：</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="title" name="title"
                                   placeholder="请输入明细标题">
                        </div>
                    </div>
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="sequence" class="col-sm-3 control-label">序号：</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" id="sequence" name="sequence"
                                   placeholder="请输入明细序号">
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
