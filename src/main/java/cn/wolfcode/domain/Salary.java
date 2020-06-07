package cn.wolfcode.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;

@Getter
@Setter
public class Salary {
    private Long id;

    private Long money;

    private Integer year;

    private BigDecimal month;

    private Employee employee;

    private SystemDictionaryItem payout;
    public String getJson(){
        HashMap map = new HashMap();
        map.put("id",id);
        map.put("month",month);
        map.put("year",year);
        map.put("money",money);
        if(employee!=null){
            map.put("employeeId",employee.getId());
        }
        if(payout!=null){
            map.put("payoutId",payout.getId());
        }
        return JSON.toJSONString(map);
    }

}