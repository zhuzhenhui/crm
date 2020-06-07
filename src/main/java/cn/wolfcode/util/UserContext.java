package cn.wolfcode.util;

import cn.wolfcode.domain.Employee;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.lang.model.element.NestingKind;
import javax.servlet.http.HttpSession;
import java.util.List;

public abstract class UserContext {

    /**
     * 从subject 中获取当前用户 shiro中自带
     * @return
     */
    public static Employee getCurrentUser() {
        Subject subject = SecurityUtils.getSubject();
        return (Employee) subject.getPrincipal();
    }
}
