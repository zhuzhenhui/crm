<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>重置密码</title>
    <#include "../common/link.ftl" >
    <script>
        $(function () {
            $('.btn-primary').click(function () {
                var newPassword = $('#editForm').serialize();
                $.post('/employee/resetPassword.do', newPassword, function (data) {// jsonResult
                    if (data.success) {//加载成功
                        window.location.href='/employee/list.do';//重新加载当前页面
                        alert(data.msg);//成功提示
                    } else {
                        alert(data.msg);//弹出错误提示
                    }
                });
            });
        });
    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <#include "../common/navbar.ftl" >
    <!--菜单回显-->
    <#assign currentMenu="employee"/>
    <#include "../common/menu.ftl" >
    <div class="content-wrapper">
        <section class="content-header">
            <h1>重置密码</h1>
        </section>
        <section class="content">
            <div class="box" style="padding: 30px;" >
                <!--高级查询--->
                <form class="form-horizontal" action="/employee/resetPwd.do" method="post" id="editForm" >
                    <input type="hidden"  name="id" value="${(employee.id)!}">
                    <div class="form-group" style="text-align: center;">
                       <h3>您正在重置员工${(employee.name)!}的密码</h3>
                    </div>
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="sn" class="col-sm-3 control-label">新密码：</label>
                        <div class="col-sm-6">
                            <input type="password" class="form-control"  name="newPassword"
                                   placeholder="请输入新密码">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-6">
                            <button id="submitBtn" type="button" class="btn btn-primary">确定重置</button>
                        </div>
                    </div>
                </form>

            </div>
        </section>
    </div>
    <#include "../common/footer.ftl" >
</div>
</body>
</html>
