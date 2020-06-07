package cn.wolfcode.qo;

import cn.wolfcode.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class CustomerReportQueryObject extends QueryObject {
    //动态查询的分组 和 分类的数值
    private String groupType = "e.name";

    //时间的开始
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    //时间的结束
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    public Date getEndDate() { // 获取结束时间当天最晚的时候
        return DateUtil.getEndDate(endDate);
    }
}
