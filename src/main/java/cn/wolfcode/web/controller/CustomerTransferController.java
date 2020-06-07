package cn.wolfcode.web.controller;
import cn.wolfcode.domain.CustomerTransfer;
import cn.wolfcode.domain.Employee;
import cn.wolfcode.qo.CustomerQueryObject;
import cn.wolfcode.service.ICustomerTransferService;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.util.JsonResult;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/customerTransfer")
public class CustomerTransferController {

    @Autowired
    private ICustomerTransferService customerTransferService;


    @RequiresPermissions(value = {"customerTransfer:list","移交历史页面"},logical = Logical.OR)
    @RequestMapping("/list")
    public String list(Model model, @ModelAttribute("qo") CustomerQueryObject qo){
        Subject subject = SecurityUtils.getSubject();
        //System.out.println(subject);
        //如果不是管理员 和 经理的情况下通过员工id找到自己的客户
        if(!(subject.hasRole("Admin")||subject.hasRole("Market_Manager"))) {
            Employee employee = (Employee) subject.getPrincipal();
            System.out.println(employee);
            qo.setSellerId(employee.getId());
        }

        PageInfo<CustomerTransfer> pageInfo = customerTransferService.query(qo);
        model.addAttribute("pageInfo", pageInfo);
        return "customerTransfer/list";
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value = {"customerTransfer:delete","移交历史删除"},logical = Logical.OR)
    @ResponseBody
    public JsonResult delete(Long id){
        if (id != null) {
            customerTransferService.delete(id);
        }
        return new JsonResult();
    }


    @RequestMapping("/saveOrUpdate")
    @RequiresPermissions(value = {"customerTransfer:saveOrUpdate","移交历史新增/编辑"},logical = Logical.OR)
    @ResponseBody
    public JsonResult saveOrUpdate(CustomerTransfer customerTransfer){
        if (customerTransfer.getId() != null) {
            customerTransferService.update(customerTransfer);
        }else {
            customerTransferService.save(customerTransfer);
        }
        return new JsonResult("移交成功,2秒后自动刷新...",true);
    }
}
