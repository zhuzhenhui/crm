package cn.wolfcode.util;



import lombok.Data;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class ExpressionsUtil {
    private ExpressionsUtil() {}

    public static String expressionFind(Class aClass, Method method) {
        String simpleName = aClass.getSimpleName();//获取控制器名
        simpleName = simpleName.replace("Controller", "");//去除Controller
        simpleName = StringUtils.uncapitalize(simpleName);//把首字母变成小写
        String methodName = method.getName();
        return simpleName + ":" + methodName;//拼接权限表达
    }
}
