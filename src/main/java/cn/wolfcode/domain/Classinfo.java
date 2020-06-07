package cn.wolfcode.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@ToString
public class Classinfo {
    private Long id;

    private String name;

    private Integer number;

    private Long employee_id;
    private Teacher teacher;//获取到老師里需要的内容
    private List<Teacher> teachers = new ArrayList<>();//员工拥有的角色
    //获取当前部门对象对应的json字符串
    public String getJson() {
        HashMap<String, Object> map = new HashMap<>();
        //页面需要啥就给啥
        map.put("id",id);
        map.put("name",name);
        map.put("number",number);
        map.put("employee_id",employee_id);
        map.put("teacher",teacher);
        //如果直接扔对象进去  就是死循环
        return JSON.toJSONString(map);
    }

}