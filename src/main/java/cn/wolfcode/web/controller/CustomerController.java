package cn.wolfcode.web.controller;


import cn.wolfcode.domain.Customer;
import cn.wolfcode.domain.Employee;
import cn.wolfcode.domain.SystemDictionaryItem;
import cn.wolfcode.qo.CustomerQueryObject;
import cn.wolfcode.service.ICustomerService;
import cn.wolfcode.service.IEmployeeService;
import cn.wolfcode.service.ISystemDictionaryItemService;
import cn.wolfcode.util.JsonResult;
import cn.wolfcode.util.UserContext;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
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
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;

    @RequiresPermissions(value = {"customer:potentialList","客户页面"},logical = Logical.OR)
    @RequestMapping("/potentialList")
    public String potentialList(Model model, @ModelAttribute("qo") CustomerQueryObject qo){
        //只查询潜在的客户状态的数据
        qo.setStatus(Customer.STATUS_COMMON);
        //管理员或者经理登录的时候可以看所有的数据, 如果只是普通的销售人员的后只能看到自己的用户
        Subject subject = SecurityUtils.getSubject();
        System.out.println(subject);
        //如果不是管理员 和 经理的情况下通过员工id找到自己的客户
        if(!(subject.hasRole("Admin")||subject.hasRole("Market_Manager"))) {
            Employee employee = (Employee) subject.getPrincipal();
            System.out.println(employee);
            qo.setSellerId(employee.getId());
        }
        //针对数据字典的工作类
        List<SystemDictionaryItem> jobs =  systemDictionaryItemService.selectByParentSn("job");
        model.addAttribute("jobs",jobs);
        //针对数据字典的来源类
        List<SystemDictionaryItem> sources =  systemDictionaryItemService.selectByParentSn("source");
        model.addAttribute("sources",sources);
        //针对数据字典的交流类
        List<SystemDictionaryItem> ccts =  systemDictionaryItemService.selectByParentSn("communicationMethod");
        model.addAttribute("ccts",ccts);
        //根据角色编码查询拥有该角色的员工 Market Market_Manager
        List<Employee> sellers = employeeService.selectByRoleSn("Market_Manager","Market");
        model.addAttribute("sellers",sellers);
        //获取所有的客户状态
        List<Customer> status = customerService.selectStatus();
        model.addAttribute("status",status);
        PageInfo<Customer> pageInfo = customerService.query(qo);
        model.addAttribute("pageInfo", pageInfo);
        return "customer/potentialList";
    }
    @RequiresPermissions(value = {"customer:list","所有客户页面"},logical = Logical.OR)
    @RequestMapping("/list")
    public String list(Model model, @ModelAttribute("qo") CustomerQueryObject qo){
        //针对数据字典的工作类
        List<SystemDictionaryItem> jobs =  systemDictionaryItemService.selectByParentSn("job");
        model.addAttribute("jobs",jobs);
        //针对数据字典的来源类
        List<SystemDictionaryItem> sources =  systemDictionaryItemService.selectByParentSn("source");
        model.addAttribute("sources",sources);
        //针对数据字典的交流类
        List<SystemDictionaryItem> ccts =  systemDictionaryItemService.selectByParentSn("communicationMethod");
        model.addAttribute("ccts",ccts);
        //根据角色编码查询拥有该角色的员工 Market Market_Manager
        List<Employee> sellers = employeeService.selectByRoleSn("Market_Manager","Market");
        model.addAttribute("sellers",sellers);
        //获取所有的客户状态
        List<Customer> status = customerService.selectStatus();
        model.addAttribute("status",status);
        PageInfo<Customer> pageInfo = customerService.query(qo);
        model.addAttribute("pageInfo", pageInfo);
        return "customer/list";
    }
    @RequiresPermissions(value = {"customer:failList","失败客户页面"},logical = Logical.OR)
    @RequestMapping("/failList")
    public String failList(Model model, @ModelAttribute("qo") CustomerQueryObject qo){
        //只查询失败的客户状态的数据
        qo.setStatus(Customer.STATUS_FAIL);
        //管理员或者经理登录的时候可以看所有的数据, 如果只是普通的销售人员的后只能看到自己的用户
        Subject subject = SecurityUtils.getSubject();
        System.out.println(subject);
        //如果不是管理员 和 经理的情况下通过员工id找到自己的客户
        if(!(subject.hasRole("Admin")||subject.hasRole("Market_Manager"))) {
            Employee employee = (Employee) subject.getPrincipal();
            System.out.println(employee);
            qo.setSellerId(employee.getId());
        }
        //针对数据字典的工作类
        List<SystemDictionaryItem> jobs =  systemDictionaryItemService.selectByParentSn("job");
        model.addAttribute("jobs",jobs);
        //针对数据字典的来源类
        List<SystemDictionaryItem> sources =  systemDictionaryItemService.selectByParentSn("source");
        model.addAttribute("sources",sources);
        //针对数据字典的交流类
        List<SystemDictionaryItem> ccts =  systemDictionaryItemService.selectByParentSn("communicationMethod");
        model.addAttribute("ccts",ccts);
        //根据角色编码查询拥有该角色的员工 Market Market_Manager
        List<Employee> sellers = employeeService.selectByRoleSn("Market_Manager","Market");
        model.addAttribute("sellers",sellers);
        //获取所有的客户状态
        List<Customer> status = customerService.selectStatus();
        model.addAttribute("status",status);
        PageInfo<Customer> pageInfo = customerService.query(qo);
        model.addAttribute("pageInfo", pageInfo);
        return "customer/failList";
    }
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"customer:delete","客户删除"},logical = Logical.OR)
    @ResponseBody
    public JsonResult delete(Long id){
        if (id != null) {
            customerService.delete(id);
        }
        return new JsonResult("操作成功2秒自动刷新..",true);
    }


    @RequestMapping("/saveOrUpdate")
    @RequiresPermissions(value = {"customer:saveOrUpdate","客户新增/编辑"},logical = Logical.OR)
    @ResponseBody
    public JsonResult saveOrUpdate(Customer customer){
        if (customer.getId() != null) {
            customerService.update(customer);
        }else {
            customerService.save(customer);
        }
        return new JsonResult("添加成功,2秒后自动刷新...",true);
    }
    //必须按照插件的规定返回结果 key:valid value:true/false
    @RequestMapping("/checkName")
    @ResponseBody
    public Map<String,Boolean> checkName(String tel, Long id) {
        Map<String, Boolean> map = new HashMap<>();
        //编辑时判断是否有id 才能编辑
        if(id!=null) {
            Customer customer = customerService.get(id);
            //这个判断不能写到true里面  要不然就少了修改的逻辑一直是false
            if(tel.equals(customer.getTel())) {
                map.put("valid",true);
                return map;
            }
        }
        //根据用户名手机号查询数据是否已经存在
        Customer customer = customerService.selectTel(tel);
        map.put("valid", customer==null);
        return map;
    }
    @RequestMapping("/statusOrUpdate")
    @RequiresPermissions(value = {"customer:statusOrUpdate","客户状态的修改"},logical = Logical.OR)
    @ResponseBody
    public JsonResult saveOrUpdates(Long customerId,Integer status){
        //System.out.println(customerId);
        if (customerId != null) {
            customerService.updateStatus(customerId,status);
        }
        return new JsonResult("状态修改成功,2秒后自动刷新...",true);
    }
}
