$(function () {
    $.messager.model = {
        ok: {text: '确定'},
        cancel: {text: '取消'}
    };

})
function handlerMessage(data) {
    if (data.success) {//登录成功
        $.messager.popup(data.msg);
        setTimeout(function () {
            window.location.reload();
        },2000);
    } else {//登录失败
        //alert(data.msg);
        $.messager.popup(data.msg);
    }
}
//接收的参数让它不带[]
jQuery.ajaxSettings.traditional = true;