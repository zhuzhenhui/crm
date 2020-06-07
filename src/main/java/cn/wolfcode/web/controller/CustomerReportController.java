package cn.wolfcode.web.controller;


import cn.wolfcode.qo.CustomerReportQueryObject;
import cn.wolfcode.service.ICustomerReportService;
import cn.wolfcode.util.MessageUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/customerReport")
public class CustomerReportController {
    @Autowired
    private ICustomerReportService customerReportService;
    @RequestMapping("/list")
    @RequiresPermissions(value = {"customerReport:list","部门列表"},logical = Logical.OR)
    public String list(Model model, @ModelAttribute("qo") CustomerReportQueryObject qo) {
        PageInfo<Map> pageInfo = customerReportService.selectCustomerReport(qo);
        //List<Department> departments = departmentService.ListAll();
        //model.addAttribute("departments",departments);
        model.addAttribute("pageInfo",pageInfo);
        return "customerReport/list";
    }
    @RequestMapping("/listByBar")
    @RequiresPermissions(value = {"customerReport:listByBar","视图列表"},logical = Logical.OR)
    public String listByBar(Model model, @ModelAttribute("qo") CustomerReportQueryObject qo) {
        //查询报表相关的数据 不做分页
        List<Map> list = customerReportService.selectAll(qo);
        //提供柱状图需要的数据
        ArrayList<Object> xList = new ArrayList<>();
        ArrayList<Object> yList = new ArrayList<>();
        //遍历list 获取到每人map
        for (Map map : list) {
            xList.add(map.get("groupType"));
            yList.add(map.get("number"));
        }
        //共享数据到页面(freemarker) 不能直接显示非字符串的数据(集合 时间 布尔)
        model.addAttribute("xList", JSON.toJSONString(xList));
        model.addAttribute("yList", JSON.toJSONString(yList));
        //转换为中文后再分享
        model.addAttribute("groupTypeName", MessageUtil.changMsg(qo));
        return "customerReport/listByBar";
    }
    @RequestMapping("/listByPar")
    @RequiresPermissions(value = {"customerReport:listByBar","视图列表"},logical = Logical.OR)
    public String listByPar(Model model, @ModelAttribute("qo") CustomerReportQueryObject qo) {
        //查询报表相关的数据 不做分页
        List<Map> list = customerReportService.selectAll(qo);
        //提供柱状图需要的数据
        ArrayList<Object> legendList = new ArrayList<>();
        ArrayList<Object> seriesList = new ArrayList<>();
        //遍历list 获取到每人map
        for (Map map : list) {
            legendList.add(map.get("groupType"));
            Map temp = new HashMap();
            temp.put("value",map.get("number"));
            temp.put("name",map.get("groupType"));
            seriesList.add(temp);
        }
        //共享数据到页面(freemarker) 不能直接显示非字符串的数据(集合 时间 布尔)
        model.addAttribute("legendList", JSON.toJSONString(legendList));
        model.addAttribute("seriesList", JSON.toJSONString(seriesList));
        //转换为中文后再分享
        model.addAttribute("groupTypeName", MessageUtil.changMsg(qo));
        return "customerReport/listByPar";
    }
}
