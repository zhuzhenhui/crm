package cn.wolfcode.web.controller;
import cn.wolfcode.domain.Employee;
import cn.wolfcode.domain.Salary;
import cn.wolfcode.domain.SystemDictionaryItem;
import cn.wolfcode.qo.SalaryQueryObject;
import cn.wolfcode.service.IEmployeeService;
import cn.wolfcode.service.ISalaryService;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.service.ISystemDictionaryItemService;
import cn.wolfcode.util.JsonResult;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/salary")
public class SalaryController {

    @Autowired
    private ISalaryService salaryService;
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;
    @RequiresPermissions(value = {"salary:list","工资管理页面"},logical = Logical.OR)
    @RequestMapping("/list")
    public String list(Model model, @ModelAttribute("qo") SalaryQueryObject qo){

        //针对数据字典的工资类
        List<Employee> employees =  employeeService.ListAll();
        model.addAttribute("employees",employees);
        //针对数据字典的工资类
        List<SystemDictionaryItem> payouts =  systemDictionaryItemService.selectByParentSn("payout");
        model.addAttribute("payouts",payouts);

        PageInfo<Salary> pageInfo = salaryService.query(qo);
        model.addAttribute("pageInfo", pageInfo);
        return "salary/list";
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value = {"salary:delete","工资管理删除"},logical = Logical.OR)
    @ResponseBody
    public JsonResult delete(Long id){
        if (id != null) {
            salaryService.delete(id);
        }
        return new JsonResult("删除成功,2秒后自动刷新...",true);
    }


    @RequestMapping("/saveOrUpdate")
    @RequiresPermissions(value = {"salary:saveOrUpdate","工资管理新增/编辑"},logical = Logical.OR)
    @ResponseBody
    public JsonResult saveOrUpdate(Salary salary){
        if (salary.getId() != null) {
            salaryService.update(salary);
        }else {
            salaryService.save(salary);
        }
        return new JsonResult("操作成功,2秒后自动刷新...",true);
    }
}
