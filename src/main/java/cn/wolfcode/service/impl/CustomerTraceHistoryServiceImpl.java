package cn.wolfcode.service.impl;

import cn.wolfcode.domain.CustomerTraceHistory;
import cn.wolfcode.domain.Employee;
import cn.wolfcode.mapper.CustomerTraceHistoryMapper;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.service.ICustomerTraceHistoryService;
import cn.wolfcode.util.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomerTraceHistoryServiceImpl implements ICustomerTraceHistoryService {

    @Autowired
    private CustomerTraceHistoryMapper customerTraceHistoryMapper;


    @Override
    public void save(CustomerTraceHistory customerTraceHistory) {
        //添加跟进的用户
        Employee currentUser = UserContext.getCurrentUser();
        customerTraceHistory.setInputUser(currentUser);
        //添加跟进的时间
        customerTraceHistory.setInputTime(new Date());

        customerTraceHistoryMapper.insert(customerTraceHistory);
    }

    @Override
    public void delete(Long id) {
        customerTraceHistoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(CustomerTraceHistory customerTraceHistory) {
        customerTraceHistoryMapper.updateByPrimaryKey(customerTraceHistory);
    }

    @Override
    public CustomerTraceHistory get(Long id) {
        return customerTraceHistoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<CustomerTraceHistory> listAll() {
        return customerTraceHistoryMapper.selectAll();
    }

    @Override
    public PageInfo<CustomerTraceHistory> query(QueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize()); //对下一句sql进行自动分页
        List<CustomerTraceHistory> customerTraceHistorys = customerTraceHistoryMapper.selectForList(qo); //里面不需要自己写limit
        return new PageInfo<CustomerTraceHistory>(customerTraceHistorys);
    }
}
