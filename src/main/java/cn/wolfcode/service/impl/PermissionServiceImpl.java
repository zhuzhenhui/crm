package cn.wolfcode.service.impl;

import cn.wolfcode.domain.Permission;
import cn.wolfcode.mapper.PermissionMapper;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.service.IPermissionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Service
public class PermissionServiceImpl implements IPermissionService{
    @Autowired
    private PermissionMapper permissionMapper;
    @Override
    public void delete(Long id) {
        permissionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Permission get(Long id) {
        return permissionMapper.selectByPrimaryKey(id);
    }
    @Override
    public List<Permission> ListAll() {
        return permissionMapper.selectAll();
    }
    //分页查询
    @Override
    public PageInfo<Permission> query(QueryObject qo) {
        //调用Mapper接口实现类对象的方法查询数据,封装 成PageResult 对象返回
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List<Permission> permissions = permissionMapper.selectForList(qo);
        return new PageInfo<>(permissions);
    }
    @Autowired
    private ApplicationContext ctx; //spring 上下文对象
    @Override
    public void reload() {
        //获取所有的权限里面的值做对比
        List<String> expressions = permissionMapper.selectAllExpression();
        //根据注解从容器中获取多个bean 对象
        Map<String, Object> beans = ctx.getBeansWithAnnotation(Controller.class);
        for (Object controller : beans.values()) {
            //获取controller 的字节码对象
            Class aClass = controller.getClass().getSuperclass();
            //获取每个controller 的方法
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                //判断方法上是否有贴权限注解
                RequiresPermissions annotation = method.getAnnotation(RequiresPermissions.class);
                if (annotation != null) {
                    //如果有, 就需要处理
                    //获取权限表达式一
                    String expression = annotation.value()[0];
                    //String expression = annotation.expression();//权限表达式
                    //String expression = ExpressionsUtil.expressionFind(aClass, method);
                    if(!expressions.contains(expression)) {
                        String name = annotation.value()[1];//权限名称
                        //拿到权限注解中的属性,封装成权限对象
                        Permission permission = new Permission();
                        permission.setName(name);
                        permission.setExpression(expression);
                        //把权限存到数据中
                        permissionMapper.insert(permission);
                    }
                }
                //没有就不用处理
            }
        }
    }

    @Override
    public List<String> selectExpressionByEmpId(Long id) {
        return permissionMapper.selectExpressionByEmpId(id);
    }
}
