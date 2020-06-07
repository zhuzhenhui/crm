package cn.wolfcode.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

@Getter
@Setter
public class Courseorder {
    private Long id;

    private Date inputTime;
    //潜在的客户
    private Customer customer;
    //购买的课程
    private SystemDictionaryItem course;

    private BigDecimal money;
    public String getJson(){
        HashMap map = new HashMap();
        map.put("id",id);
        map.put("money",money);
        if(customer!=null){
            map.put("customerId",customer.getId());
        }
        if(course!=null){
            map.put("courseId",course.getId());
        }
        return JSON.toJSONString(map);
    }

}