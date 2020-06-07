package cn.wolfcode.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@Setter
@Getter
@ToString
public class SystemDictionaryItem {
    private Long id;

    private Long parentId;

    private String title;

    private Integer sequence;
    //获取当前字典明细对象对应的json字符串
    public String getJson() {
        HashMap<String, Object> map = new HashMap<>();
        //页面需要啥就给啥
        map.put("id",id);
        map.put("parentId",parentId);
        map.put("title",title);
        map.put("sequence",sequence);
        //如果直接扔对象进去  就是死循环
        return JSON.toJSONString(map);
    }
}