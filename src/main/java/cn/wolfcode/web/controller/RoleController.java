package cn.wolfcode.web.controller;

import cn.wolfcode.domain.Permission;
import cn.wolfcode.domain.Role;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.service.IPermissionService;
import cn.wolfcode.service.IRoleService;
import cn.wolfcode.util.RequiredPermission;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IPermissionService permissionServicee;
    @RequestMapping("/list")
    @RequiresPermissions(value = {"role:list","角色显示"},logical = Logical.OR)
    public String list(Model model, @ModelAttribute("qo") QueryObject qo) {
        //利用分页插件的数据分页
        PageInfo<Role> pageInfo = roleService.query(qo);
        model.addAttribute("pageInfo",pageInfo);
        return "role/list";
    }
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"role:delete","角色删除"},logical = Logical.OR)
    public String delete(Long id) {
        if (id != null) {
            roleService.delete(id);
        }
        return "redirect:/role/list.do";
    }
    @RequestMapping("/input")
    @RequiresPermissions(value = {"role:input","角色添加/修改:显示"},logical = Logical.OR)
    public String input(Long id, Model model) {
        List<Permission> permissions = permissionServicee.ListAll();
        model.addAttribute("permissions",permissions);
        if (id != null) {
            //去修改  把修改的部门数据查询查出来
            Role role = roleService.get(id);
            model.addAttribute("role",role);
        }
        return "role/input";
    }
    @RequestMapping("/saveOrUpdate")
    @RequiresPermissions(value = {"role:saveOrUpdate","角色添加/修改"},logical = Logical.OR)
    public String saveOrUpdate(Role role, Long[] ids) {

        //如果有id  就执行修改方法 没有就执行 添加方法
        if (role.getId() != null) {
            roleService.update(role,ids);
        } else {

            roleService.save(role,ids);
        }
        return "redirect:/role/list.do";
    }
}
