package cn.wolfcode.service;

import cn.wolfcode.domain.Classinfo;
import cn.wolfcode.qo.CustomerQueryObject;
import cn.wolfcode.qo.CustomerReportQueryObject;
import cn.wolfcode.qo.QueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface ICustomerReportService {

    //分页查询
    PageInfo<Map> selectCustomerReport(CustomerReportQueryObject qo);

    List<Map> selectAll(CustomerReportQueryObject qo);
}
