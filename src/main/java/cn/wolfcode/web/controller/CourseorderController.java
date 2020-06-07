package cn.wolfcode.web.controller;
import cn.wolfcode.domain.Courseorder;
import cn.wolfcode.domain.Customer;
import cn.wolfcode.domain.SystemDictionaryItem;
import cn.wolfcode.qo.CourseorderQueryObject;
import cn.wolfcode.service.ICourseorderService;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.service.ICustomerService;
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
@RequestMapping("/courseorder")
public class CourseorderController {

    @Autowired
    private ICourseorderService courseorderService;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;

    @RequiresPermissions(value = {"courseorder:list","成交订单页面"},logical = Logical.OR)
    @RequestMapping("/list")
    public String list(Model model, @ModelAttribute("qo") CourseorderQueryObject qo){

        //显示潜在的客户
        List<Customer> customers = customerService.selectStatu(Customer.STATUS_COMMON);
        model.addAttribute("customers", customers);

        List<Customer> customer = customerService.selectStatu(Customer.STATUS_NORMAL);
        model.addAttribute("customer", customer);
        //查找下拉框的内容
        List<SystemDictionaryItem> subjects = systemDictionaryItemService.selectByParentSn("subject");
        model.addAttribute("subjects", subjects);
        PageInfo<Courseorder> pageInfo = courseorderService.query(qo);
        model.addAttribute("pageInfo", pageInfo);
        return "courseorder/list";
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value = {"courseorder:delete","成交订单删除"},logical = Logical.OR)
    @ResponseBody
    public JsonResult delete(Long id){
        if (id != null) {
            courseorderService.delete(id);
        }
        return new JsonResult();
    }


    @RequestMapping("/saveOrUpdate")
    @RequiresPermissions(value = {"courseorder:saveOrUpdate","成交订单新增/编辑"},logical = Logical.OR)
    @ResponseBody
    public JsonResult saveOrUpdate(Courseorder courseorder){
        if (courseorder.getId() != null) {
            courseorderService.update(courseorder);
        }else {
            courseorderService.save(courseorder);
        }
        return new JsonResult("操作成功,2秒后自动刷新...",true);
    }
}
