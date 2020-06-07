package cn.wolfcode.mapper;


import cn.wolfcode.qo.CustomerReportQueryObject;
import java.util.List;
import java.util.Map;

public interface CustomerReportMapper {

    List <Map> selectCustomerReport(CustomerReportQueryObject qo);

    List<Map> selectAll(CustomerReportQueryObject qo);
}