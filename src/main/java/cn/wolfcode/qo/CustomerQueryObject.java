package cn.wolfcode.qo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerQueryObject extends QueryObject {
    //状态
    private Integer status;
    //销售人员的id
    private Long sellerId = -1L;
}
