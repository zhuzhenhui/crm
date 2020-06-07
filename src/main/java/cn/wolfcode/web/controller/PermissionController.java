package cn.wolfcode.web.controller;

import cn.wolfcode.domain.Permission;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.service.IPermissionService;
import cn.wolfcode.util.JsonResult;
import cn.wolfcode.util.RequiredPermission;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private IPermissionService permissionService;
    @RequestMapping("/list")
    @RequiresPermissions(value = {"permission:list","权限显示"},logical = Logical.OR)
    public String list(Model model, @ModelAttribute("qo") QueryObject qo) {
        PageInfo<Permission> pageInfo = permissionService.query(qo);
        //List<Permission> permissions = permissionService.ListAll();
        //model.addAttribute("permissions",permissions);
        model.addAttribute("pageInfo",pageInfo);
        return "permission/list";
    }
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"permission:delete","权限删除"},logical = Logical.OR)
    public String delete(Long id) {
        if (id != null) {
            permissionService.delete(id);
        }
        return "redirect:/permission/list.do";
    }
    @RequestMapping("/reload")
    @RequiresPermissions(value = {"permission:reload","权限加载"},logical = Logical.OR)
    @ResponseBody // 返回数据给前端
    public JsonResult reload() {
        try {
            permissionService.reload();//执行业务
            return new JsonResult();//成功
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult("加载失败,请联系管理员",false);
        }
    }
}
