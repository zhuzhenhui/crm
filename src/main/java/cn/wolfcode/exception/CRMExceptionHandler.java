package cn.wolfcode.exception;

import cn.wolfcode.util.JsonResult;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 对控制器进行增强
 */
@ControllerAdvice
public class CRMExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Object handlerException(Exception ex, HandlerMethod handlerMethod, HttpServletResponse response) throws IOException {
        ex.printStackTrace();
        //handlerMethod就是出现异常的处理方法
        //如果ajax对应的方法返回Json 数据(贴了@ResponseBody注解)
        if(handlerMethod.hasMethodAnnotation(ResponseBody.class)) {
            JsonResult json = new JsonResult("操作错误, 请联系管理员!",false);
            //设置编码格式
            response.setContentType("application/json;charset=UTF-8");
            //但是也要返回同步的页面数据json 所以要用response返回
            response.getWriter().print(JSON.toJSONString(json));
            return null;
        } else {
            //返回错误页面
            return "common/error";
        }
    }
    @ExceptionHandler(UnauthorizedException.class)
    public Object handlerShiroException(Exception ex, HandlerMethod handlerMethod, HttpServletResponse response) throws IOException {
        ex.printStackTrace();
        //handlerMethod就是出现异常的处理方法
        //如果ajax对应的方法返回Json 数据(贴了@ResponseBody注解)
        if(handlerMethod.hasMethodAnnotation(ResponseBody.class)) {
            JsonResult json = new JsonResult("你没有该操作的权限表!",false);
            //设置编码格式
            response.setContentType("application/json;charset=UTF-8");
            //但是也要返回同步的页面数据json 所以要用response返回
            response.getWriter().print(JSON.toJSONString(json));
            return null;
        } else {
            //返回错误页面
            return "common/nopermission";
        }
    }
}
