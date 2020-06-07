package cn.wolfcode.shiro;

import cn.wolfcode.domain.Employee;
import cn.wolfcode.mapper.PermissionMapper;
import cn.wolfcode.mapper.RoleMapper;
import cn.wolfcode.service.IEmployeeService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;

@Component("crmRealm")
public class CrmRealm extends AuthorizingRealm{
    @Autowired
    public IEmployeeService employeeService;
    @Autowired
    public RoleMapper roleMapper;
    @Autowired
    public PermissionMapper permissionMapper;
    @Autowired
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {//加载bean
        super.setCredentialsMatcher(credentialsMatcher);
    }

    /**
     * 提供认证信息
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取认证信息
        //暂时使用假数据来模拟真实的账号和密码
        /*String username = "admin";
        String password = "1";*/
        //判断token里面的用户名,在数据库中是否存在 getPrincipal() 方法其实就是获取token 里面的用户名
        Employee employee = employeeService.selectName((String) token.getPrincipal());
        //如果账号正确, 返回一个AuthenticationInfo 对象
        if (employee != null) {
            //用户对象(自动绑定subject),密码,加盐(用户名)当前数的名字(数据源标记)employee.getName()
            //如果返回的不为空(带有正确的密码) shiro 就会自己去对比验证
            return  new SimpleAuthenticationInfo(employee,employee.getPassword(),
                    ByteSource.Util.bytes(employee.getName()),"crmRealm");
        }
        //返回的是null 的话  shiro 会抛出相应的异常
        return null;
    }

    /**
     * 提供角色权限信息
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("zhazhahui");

        //该方法返回的结果中包含了那些角色和权限数据,那么当前的subject 主体就就拥有那些角色和权限
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获取当前登录用的id
        Employee employee = (Employee)principals.getPrimaryPrincipal();
        if (employee.isAdmin()) {//管理员
            info.addStringPermission("*:*");//所用的权限
            info.addRole("Admin");//管理员的角色,咱时还没有用到
        } else {

            //根据员工id查询该员工拥有权限
            List<String> permissions = permissionMapper.selectExpressionByEmpId(employee.getId());
            info.addStringPermissions(permissions);
            ////根据员工id查询该员工拥有角色
            List<String> roles = roleMapper.selectSnByEmpId(employee.getId());
            info.addRoles(roles);
        }
        return info;
    }
}
