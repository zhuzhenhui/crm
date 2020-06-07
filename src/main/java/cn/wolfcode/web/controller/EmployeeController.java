package cn.wolfcode.web.controller;

import cn.wolfcode.domain.Department;
import cn.wolfcode.domain.Employee;
import cn.wolfcode.domain.Role;
import cn.wolfcode.qo.EmployeeQueryObject;
import cn.wolfcode.service.IDepartmentService;
import cn.wolfcode.service.IEmployeeService;
import cn.wolfcode.service.IRoleService;
import cn.wolfcode.util.JsonResult;
import cn.wolfcode.util.RequiredPermission;
import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IRoleService roleService;
    @RequestMapping("/list")
    @RequiresPermissions(value = {"employee:list","员工显示"},logical = Logical.OR)
    public String list(Model model, @ModelAttribute("qo") EmployeeQueryObject qo) {
        //Subject subject = SecurityUtils.getSubject();
        //System.out.println("判断用户是否有role1 角色" + subject.hasRole("HR_MGR"));
        //System.out.println("判断用户是否有user:update权限" + subject.isPermitted("employee:list"));
        PageInfo<Employee> pageInfo = employeeService.query(qo);
        List<Department> departments = departmentService.ListAll();
        model.addAttribute("departments",departments);
        model.addAttribute("pageInfo",pageInfo);
        return "employee/list";
    }
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"employee:delete","员工删除"},logical = Logical.OR)
    @ResponseBody
    public JsonResult delete(Long id) {
            //如果有id  就执行修改方法 没有就执行 添加方法
            if (id != null) {
                employeeService.delete(id);
            }
            return new JsonResult("操作成功2秒自动刷新...",true);
        /*if (id != null) {
            employeeService.delete(id);
        }
        return "redirect:/employee/list.do";*/
    }
    @RequestMapping("/input")
    @RequiresPermissions(value = {"employee:input","员工添加/修改:显示"},logical = Logical.OR)
    public String input(Long id, Model model) {
        List<Role> roles = roleService.listAll();
        model.addAttribute("roles",roles);
        List<Department> departments = departmentService.ListAll();
        model.addAttribute("departments",departments);
        if (id != null) {
            //去修改  把修改的部门数据查询查出来
            Employee employee = employeeService.get(id);
            model.addAttribute("employee",employee);
        }
        return "employee/input";
    }
    @RequestMapping("/saveOrUpdate")
    @RequiresPermissions(value = {"employee:saveOrUpdate","员工添加/修改"},logical = Logical.OR)
    @ResponseBody
    public JsonResult saveOrUpdate(Employee employee,Long[] ids) {
            //如果有id  就执行修改方法 没有就执行 添加方法
            if (employee.getId() != null) {
                employeeService.update(employee,ids);
            } else {
                employeeService.save(employee,ids);
            }
            return new JsonResult("操作成功2秒自动刷新..",true);
        /*//如果有id  就执行修改方法 没有就执行 添加方法
        if (employee.getId() != null) {
            employeeService.update(employee,ids);
        } else {
            employeeService.save(employee,ids);
        }
        return "redirect:/employee/list.do";*/
    }
    @RequestMapping("/batchDelete")
    @RequiresPermissions(value = {"employee:batchDelete","员工批量删除"},logical = Logical.OR)
    @ResponseBody
    public JsonResult batchDelete(Long[] ids) {
            //如果有id  就执行修改方法 没有就执行 添加方法
            if(ids != null && ids.length > 0) {
                employeeService.batchDelete(ids);
            }
            return new JsonResult("操作成功2秒自动刷新..",true);
    }
    //必须按照插件的规定返回结果 key:valid value:true/false
    @RequestMapping("/checkName")
    @ResponseBody
    public Map<String,Boolean> checkName(String name, Long id) {
        Map<String, Boolean> map = new HashMap<>();
        //编辑时判断是否有id 才能编辑
        if(id!=null) {
            Employee employee = employeeService.get(id);
            //这个判断不能写到true里面  要不然就少了修改的逻辑一直是false
            if(name.equals(employee.getName())) {
                map.put("valid",true);
                return map;
            }
        }
        //根据用户名查询数据是否已经存在
        Employee employee = employeeService.selectName(name);
        map.put("valid", employee==null);
        return map;
    }
    @RequestMapping("/updatePwds")
    @RequiresPermissions(value = {"employee:updatePwds","员工修改密码"},logical = Logical.OR)
    @ResponseBody
    public JsonResult updatePwds(String oldPassword,String newPassword, Long id) {
            Employee employee = employeeService.get(id);
            //新密码和旧密码相同
            //System.out.println(employee);
            //System.out.println(employee.getPassword());
            //加密秘密 用户名是盐
            Md5Hash hash = new Md5Hash(oldPassword,employee.getName());

            Md5Hash newHash = new Md5Hash(newPassword,employee.getName());
            //employee.setPassword(newHash.toString());
            if (employee.getPassword().equals(hash.toString())) {
                System.out.println(oldPassword);
                employeeService.updatePassword(employee.getId(), newHash.toString());
                return new JsonResult();//修改成功
            }
            return new JsonResult("原密码错误!!!",false);
    }
    @RequestMapping("/updatePwd")
    @RequiresPermissions(value = {"employee:updatePwd","员工修改密码显示"},logical = Logical.OR)
    public String updatePwd() {

       return "employee/updatePwd";
    }
    @RequestMapping("/resetPwd")
    @RequiresPermissions(value = {"employee:resetPwd","管理员修改密码显示"},logical = Logical.OR)
    public String resetPwd(Long id, Model model) {
        if (id != null) {
            System.out.println(id);
            Employee employee = employeeService.get(id);
            model.addAttribute("employee",employee);
        }
        return "employee/resetPwd";
    }
    @RequestMapping("/resetPassword")
    @RequiresPermissions(value = {"employee:resetPassword","管理员修改密码"},logical = Logical.OR)
    @ResponseBody
    public JsonResult resetPassword(Long id,String newPassword) {

        //修改别人不能用session里面的数据id 要不然会出现之前的名字是第一次的
        Employee employee = employeeService.get(id);
        //System.out.println(employee.getPassword());
        //System.out.println(newPassword);
        //加密秘密 用户名是盐
        Md5Hash hash = new Md5Hash(newPassword,employee.getName());
        //employee.setPassword(hash.toString());
        //判断密码是否是之前的密码
        if (!employee.getPassword().equals(hash.toString())) {
            employeeService.updatePassword(id, hash.toString());
            return new JsonResult("修改密码成功",true);//修改成功
        }
        return new JsonResult("旧密码和新密码不可以一样!!!", false);
    }

    @RequestMapping("/exportXls")
    public void exportXls(HttpServletResponse response) throws IOException {


        //文件下载的响应头（让浏览器访问资源的的时候以下载的方式打开）
        response.setHeader("Content-Disposition","attachment;filename=employee.xlsx");

        //查询所有的员工 1
        List<Employee> employees = employeeService.ListAll();

        // 根据用户传入字段 假设我们只要导出 date  1
        Set<String> includeColumnFiledNames = new HashSet<String>();
        includeColumnFiledNames.add("name");
        includeColumnFiledNames.add("email");
        includeColumnFiledNames.add("age");
        EasyExcel.write(response.getOutputStream(), Employee.class).sheet("员工通讯录")
                .includeColumnFiledNames(includeColumnFiledNames)
                .doWrite(employees);


        /*Workbook wb = employeeService.exportXls();
        //把文件输出到浏览器
        wb.write(response.getOutputStream());*/


    }
    @RequestMapping("/importXls")
    @ResponseBody
    public JsonResult importXls(MultipartFile file) throws IOException {
        //导入文本的内容
        employeeService.importXls(file);

        return  new JsonResult("导入成功",true);
    }
}
