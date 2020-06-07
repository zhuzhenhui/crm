package cn.wolfcode.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashMap;

@Setter
@Getter
public class CustomerTraceHistory {
    private Long id;
    //跟进时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date traceTime;
    //跟进内容
    private String traceDetails;
    //跟进方式
    private SystemDictionaryItem traceType;
    //跟进结果
    private Integer traceResult;
    //客户
    private Customer customer;
    //录入人
    private Employee inputUser;
    //录入时间
    private Date inputTime;
    public String getJson(){
        HashMap map = new HashMap();
        map.put("id",id);
        return JSON.toJSONString(map);
    }
    //重新针对页面的数字转为中文文字
    public String getTraceResults() {
        switch (traceResult) {
            case 3:
                return "优";
            case 2:
                return "中";
            case 1:
                return "差";
            default:
                return "成交";
        }
    }
}