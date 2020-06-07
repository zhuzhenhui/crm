<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>修改密码</title>
    <#include "../common/link.ftl">
    <script>
        $(function () {
            $('.btn-primary').click(function () {
                //获取表单的内容
                var employee = $('#editForm').serialize();
                //console.log(employee);
                //通过异步的请求
                $.post("/employee/updatePwds.do",employee,function (data) {
                    if (data.success) {//修改成功
                        window.location.href = "/logout.do";

                    } else {//修改失败
                        alert(data.msg);
                        console.log(2);
                    }
                });
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
            <h1>修改密码</h1>
        </section>
        <section class="content">
            <div class="box" style="padding: 30px;" >
                <!--高级查询--->
                <form class="form-horizontal" action="/employee/updatePwd.do" method="post" id="editForm" >
                    <input type="hidden"  name='id' value="<@shiro.principal property="id" />">
                    <div class="form-group" style="margin-top: 10px;">
                        <label for="name" class="col-sm-3 control-label">原密码：</label>
                        <div class="col-sm-6">
                            <input type="password" class="form-control"  name="oldPassword"
                                   placeholder="请输入原密码">
                        </div>
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
                            <button id="submitBtn" type="button" class="btn btn-primary">确定修改</button>
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
