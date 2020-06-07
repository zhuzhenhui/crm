package cn.wolfcode.web.controller;

import cn.wolfcode.domain.SystemDictionary;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.service.ISystemDictionaryService;
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

@Controller
@RequestMapping("/systemDictionary")
public class SystemDictionaryController {
    @Autowired
    private ISystemDictionaryService systemDictionaryService;
    @RequestMapping("/list")
    public String list(Model model, @ModelAttribute("qo") QueryObject qo) {
        PageInfo<SystemDictionary> pageInfo = systemDictionaryService.query(qo);
        //List<SystemDictionary> systemDictionarys = systemDictionaryService.ListAll();
        //model.addAttribute("systemDictionarys",systemDictionarys);
        model.addAttribute("pageInfo",pageInfo);
        return "systemDictionary/list";
    }
    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult delete(Long id) {
            //如果有id  就执行修改方法 没有就执行 添加方法
            if (id != null) {
                systemDictionaryService.delete(id);
            }
            return new JsonResult("操作成功2秒自动刷新..",true);
    }
    /*@RequestMapping("/input")
    @RequiredPermission(value = "部门添加/修改:显示")
    public String input(Long id, Model model) {
        if (id != null) {
            //去修改  把修改的部门数据查询查出来
            SystemDictionary systemDictionary = systemDictionaryService.get(id);
            model.addAttribute("systemDictionary",systemDictionary);
        }
        return "systemDictionary/input";
    }*/
    @RequestMapping("/saveOrUpdate")
    @ResponseBody//前端参数  修改成异步的得了
    public JsonResult saveOrUpdate(SystemDictionary systemDictionary) {
            //如果有id  就执行修改方法 没有就执行 添加方法
            if (systemDictionary.getId() != null) {
                systemDictionaryService.update(systemDictionary);
            } else {
                systemDictionaryService.save(systemDictionary);
            }
           return new JsonResult("操作成功2秒自动刷新..",true);
    }

}
