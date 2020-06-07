package cn.wolfcode.service.impl;


import cn.wolfcode.mapper.CustomerReportMapper;
import cn.wolfcode.qo.CustomerReportQueryObject;
import cn.wolfcode.service.ICustomerReportService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerReportServiceImpl implements ICustomerReportService {
    @Autowired
    private CustomerReportMapper customerReportMapper;

    //分页查询
    @Override
    public PageInfo selectCustomerReport(CustomerReportQueryObject qo) {
        //不需要手动执行count语句,由分页插件来执行
        //告诉分页插件, 当前页以及每页显示的数据就可以了,分页插件会自动算出来start的值, 放到limit
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());//会对下一句sql自动分页  顺序也不能换
        List<Map> list = customerReportMapper.selectCustomerReport(qo);//里面不需要自己写limit
        return new PageInfo<Map>(list);
    }

    @Override
    public List<Map> selectAll(CustomerReportQueryObject qo) {
        return customerReportMapper.selectAll(qo);
    }

}
