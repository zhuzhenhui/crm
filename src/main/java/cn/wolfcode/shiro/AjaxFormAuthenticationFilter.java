package cn.wolfcode.shiro;

import cn.wolfcode.util.JsonResult;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Component("ajaxFormAuthenticationFilter")//不写是默认的开头小写
public class AjaxFormAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        //使用response 输出json的数据给前端
        JsonResult json = new JsonResult();
        //设置编码格式
        response.setContentType("application/json;charset=UTF-8");
        //但是也要返回同步的页面数据json 所以要用response返回
        response.getWriter().print(JSON.toJSONString(json));

        //过滤器返回false不放行
        return false;
    }



    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        //使用response 输出json的数据给前端
        JsonResult json = new JsonResult("登录失败,用户名或秘密不匹配!",false);
        //设置编码格式
        response.setContentType("application/json;charset=UTF-8");
        //但是也要返回同步的页面数据json 所以要用response返回
        try {
            response.getWriter().print(JSON.toJSONString(json));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //过滤器返回false不放行
        return false;
    }
}
