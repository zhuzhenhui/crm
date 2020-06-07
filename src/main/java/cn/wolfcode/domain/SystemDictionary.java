package cn.wolfcode.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class SystemDictionary {
    private Long id;

    private String sn;

    private String title;

    private String intro;

    //获取当前字典目录对象对应的json字符串
    public String getJson() {
        HashMap<String, Object> map = new HashMap<>();
        //页面需要啥就给啥
        map.put("id",id);
        map.put("sn",sn);
        map.put("title",title);
        map.put("intro",intro);
        //如果直接扔对象进去  就是死循环
        return JSON.toJSONString(map);
    }

}