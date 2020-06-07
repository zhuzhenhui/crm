package cn.wolfcode.web.controller;

import cn.wolfcode.domain.Classinfo;
import cn.wolfcode.domain.Classinfo;
import cn.wolfcode.domain.Employee;
import cn.wolfcode.domain.Teacher;
import cn.wolfcode.mapper.TeacherMapper;
import cn.wolfcode.qo.EmployeeQueryObject;
import cn.wolfcode.service.IClassinfoService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/classinfo")
public class ClassinfoController {
    @Autowired
    private IClassinfoService classinfoService;
    @Autowired
    private TeacherMapper teacherMapper;
    @RequestMapping("/list")
    @RequiresPermissions(value = {"classinfo:list","班级显示"},logical = Logical.OR)
    public String list(Model model, @ModelAttribute("qo") EmployeeQueryObject qo) {
        PageInfo<Classinfo> pageInfo = classinfoService.query(qo);
        List<Teacher> teachers = teacherMapper.selectAll();
        System.out.println(teachers);
        model.addAttribute("teachers",teachers );
        model.addAttribute("pageInfo",pageInfo);
        return "classinfo/list";
    }
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"classinfo:delete","部门删除"},logical = Logical.OR)
    @ResponseBody
    public JsonResult delete(Long id) {
            //如果有id  就执行修改方法 没有就执行 添加方法
            if (id != null) {
                classinfoService.delete(id);
            }
            return new JsonResult("操作成功2秒自动刷新..",true);
    }
    /*@RequestMapping("/input")
    @RequiredPermission(value = "部门添加/修改:显示")
    public String input(Long id, Model model) {
        if (id != null) {
            //去修改  把修改的部门数据查询查出来
            Classinfo classinfo = classinfoService.get(id);
            model.addAttribute("classinfo",classinfo);
        }
        return "classinfo/input";
    }*/
    @RequestMapping("/saveOrUpdate")
    @RequiresPermissions(value = {"classinfo:saveOrUpdate","部门添加/修改"},logical = Logical.OR)
    @ResponseBody//前端参数  修改成异步的得了
    public JsonResult saveOrUpdate(Classinfo classinfo,@RequestParam("teacher.id") Long teacherId) {

        //如果有id  就执行修改方法 没有就执行 添加方法
        System.out.println(teacherId);
        if (classinfo.getId() != null) {
                classinfoService.update(classinfo,teacherId);
            } else {
                classinfoService.save(classinfo,teacherId);
            }
           return new JsonResult("操作成功2秒自动刷新..",true);
    }
}
