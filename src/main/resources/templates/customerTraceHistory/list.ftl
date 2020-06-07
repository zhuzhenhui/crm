<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>跟进管理</title>
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
                if(json) {
                    //会显数据
                    $("input[name=id]").val(json.id);
                    $("input[name=sn]").val(json.sn);
                    $("input[name=title]").val(json.title);
                    $("input[name=intro]").val(json.intro);
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
                $.post("/customerTraceHistory/saveOrUpdate.do",params,function (data) {
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
                //获取跟进的id值
                var id = $(this).data("id");
                console.log(id);
                //确认框
                $.messager.confirm('警告','是否要删除该数据?',function(){ //点击确定后的回调函数
                    $.post('/customerTraceHistory/delete.do',{"id":id},handlerMessage);
                });
            });
        });
    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <#include "../common/navbar.ftl">
    <!--菜单回显-->
    <#assign currentMenu="customerTraceHistory"/>
    <#include "../common/menu.ftl">
    <div class="content-wrapper">
        <section class="content-header">
            <h1>跟进管理</h1>
        </section>
        <section class="content">
            <div class="box">
                <!--高级查询--->
                <form class="form-inline" id="searchForm" action="/customerTraceHistory/list.do" method="post">
                    <input type="hidden" name="currentPage" id="currentPage" value="1">
                </form>
                <!--编写内容-->
                <div class="box-body table-responsive no-padding ">
                    <table class="table table-hover table-bordered">
                        <tr>
                            <th>编号</th>
                            <th>姓名</th>
                            <th>跟进日期</th>
                            <th>跟进内容</th>
                            <th>跟进方式</th>
                            <th>跟进结果</th>
                            <th>录入人</th>
                            <th>操作</th>
                        </tr>

                        <#list pageInfo.list as customerTraceHistory>
                            <tr>
                                <td>${customerTraceHistory_index+1}</td>
                                <td>${(customerTraceHistory.customer.name)!}</td>
                                <td>${(customerTraceHistory.traceTime?string('yyyy-MM-dd'))!}</td>
                                <td>${(customerTraceHistory.traceDetails)!}</td>
                                <td>${(customerTraceHistory.traceType.title)!}</td>
                                <td>${customerTraceHistory.traceResults!}</td>
                                <td>${(customerTraceHistory.inputUser.name)!}</td>
                                <td>
                                    <@shiro.hasPermission name="customerTraceHistory:delete">
                                    <a href="#" class="btn btn-danger btn-xs btn-delete" data-id='${(customerTraceHistory.id)!}'>
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

</body>
</html>
