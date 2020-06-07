package cn.wolfcode.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@Setter
@Getter
@ToString
public class Department {

    private Long id;
    private String name;
    private String sn;
    //获取当前部门对象对应的json字符串
    public String getJson() {
        HashMap<String, Object> map = new HashMap<>();
        //页面需要啥就给啥
        map.put("id",id);
        map.put("name",name);
        map.put("sn",sn);
        //如果直接扔对象进去  就是死循环
        return JSON.toJSONString(map);
    }
}
