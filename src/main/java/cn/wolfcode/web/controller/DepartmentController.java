package cn.wolfcode.web.controller;

import cn.wolfcode.domain.Department;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.service.IDepartmentService;
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
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private IDepartmentService departmentService;
    @RequestMapping("/list")
    @RequiresPermissions(value = {"department:list","部门列表"},logical = Logical.OR)
    public String list(Model model, @ModelAttribute("qo") QueryObject qo) {
        PageInfo<Department> pageInfo = departmentService.query(qo);
        //List<Department> departments = departmentService.ListAll();
        //model.addAttribute("departments",departments);
        model.addAttribute("pageInfo",pageInfo);
        return "department/list";
    }
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"department:delete","部门删除"},logical = Logical.OR)
    @ResponseBody
    public JsonResult delete(Long id) {
            //如果有id  就执行修改方法 没有就执行 添加方法
            if (id != null) {
                departmentService.delete(id);
            }
            return new JsonResult("操作成功2秒自动刷新..",true);
    }
    /*@RequestMapping("/input")
    @RequiredPermission(value = "部门添加/修改:显示")
    public String input(Long id, Model model) {
        if (id != null) {
            //去修改  把修改的部门数据查询查出来
            Department department = departmentService.get(id);
            model.addAttribute("department",department);
        }
        return "department/input";
    }*/
    @RequestMapping("/saveOrUpdate")
    @RequiresPermissions(value = {"department:saveOrUpdate","部门添加/修改"},logical = Logical.OR)
    @ResponseBody//前端参数  修改成异步的得了
    public JsonResult saveOrUpdate(Department department) {
            //如果有id  就执行修改方法 没有就执行 添加方法
            if (department.getId() != null) {
                departmentService.update(department);
            } else {
                departmentService.save(department);
            }
           return new JsonResult("操作成功2秒自动刷新..",true);
    }

}
