package cn.wolfcode.web.controller;

import cn.wolfcode.domain.SystemDictionary;
import cn.wolfcode.domain.SystemDictionaryItem;
import cn.wolfcode.qo.SystemDictionaryItemQueryObject;
import cn.wolfcode.service.ISystemDictionaryItemService;
import cn.wolfcode.service.ISystemDictionaryService;
import cn.wolfcode.util.JsonResult;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/systemDictionaryItem")
public class SystemDictionaryItemController {
    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;
    @Autowired
    private ISystemDictionaryService systemDictionaryService;
    @RequestMapping("/list")
    public String list(Model model, @ModelAttribute("qo") SystemDictionaryItemQueryObject qo) {
        PageInfo<SystemDictionaryItem> pageInfo = systemDictionaryItemService.query(qo);
        List<SystemDictionary> systemDictionarys = systemDictionaryService.ListAll();
        model.addAttribute("systemDictionarys",systemDictionarys);
        model.addAttribute("pageInfo",pageInfo);
        return "systemDictionaryItem/list";
    }
    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult delete(Long id) {
            //如果有id  就执行修改方法 没有就执行 添加方法
            if (id != null) {
                systemDictionaryItemService.delete(id);
            }
            return new JsonResult("操作成功2秒自动刷新..",true);
    }
    /*@RequestMapping("/input")
    @RequiredPermission(value = "部门添加/修改:显示")
    public String input(Long id, Model model) {
        if (id != null) {
            //去修改  把修改的部门数据查询查出来
            SystemDictionaryItem systemDictionaryItem = systemDictionaryItemService.get(id);
            model.addAttribute("systemDictionaryItem",systemDictionaryItem);
        }
        return "systemDictionaryItem/input";
    }*/
    @RequestMapping("/saveOrUpdate")
    @ResponseBody//前端参数  修改成异步的得了
    public JsonResult saveOrUpdate(SystemDictionaryItem systemDictionaryItem) {
        //针对用户填入数据为空的时候+1
        if (systemDictionaryItem.getSequence()==null) {
            //获取选定的parentId 里面的集合
            List<SystemDictionaryItem> systemDictionaryItems = systemDictionaryItemService.selectParentId(systemDictionaryItem.getParentId());
            //System.out.println(systemDictionaryItems);
            //假设一个最大的值
            int max = 0;
            //遍历里面的序号
            for (int i = 0; i < systemDictionaryItems.size() ; i++) {
                //通过id 下标获取里面的每一
                SystemDictionaryItem systemDictionaryItemList = systemDictionaryItems.get(i);
                //每一对应的序号值
                Integer sequence = systemDictionaryItemList.getSequence();
                //System.out.println(sequence);
                //拿到最大的序号
                if(sequence > max) {
                    max = sequence;
                }
            }
            //System.out.println(max);
            //获取最大的序号之后加1
            systemDictionaryItem.setSequence(max+1);
        }
        //如果有id  就执行修改方法 没有就执行 添加方法
        if (systemDictionaryItem.getId() != null) {
            //System.out.println(systemDictionaryItem);
            systemDictionaryItemService.update(systemDictionaryItem);
        } else {
            //System.out.println(systemDictionaryItem);
            systemDictionaryItemService.save(systemDictionaryItem);
        }

        return new JsonResult("操作成功2秒自动刷新..",true);
    }

}
