package cn.wolfcode.qo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalaryQueryObject extends QueryObject {

    //工资的发放方式的id
    private Long payoutId = -1L;
}
