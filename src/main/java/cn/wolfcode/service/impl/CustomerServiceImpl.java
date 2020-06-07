package cn.wolfcode.service.impl;

import cn.wolfcode.domain.Customer;
import cn.wolfcode.domain.Employee;
import cn.wolfcode.mapper.CustomerMapper;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.service.ICustomerService;
import cn.wolfcode.util.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private CustomerMapper customerMapper;


    @Override
    public void save(Customer customer) {
        //获取当前的登录用户
        /*Subject subject = SecurityUtils.getSubject();
        Employee employee = (Employee) subject.getPrincipal();*/
        Employee currentUser = UserContext.getCurrentUser();
        //设置当前登录用户为录入人  销售员
        customer.setInputUser(currentUser);
        customer.setSeller(currentUser);
        //设置录入的时间
        customer.setInputTime(new Date());
        customerMapper.insert(customer);
    }

    @Override
    public void delete(Long id) {
        customerMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Customer customer) {
        customerMapper.updateByPrimaryKey(customer);
    }

    @Override
    public Customer get(Long id) {
        return customerMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Customer> listAll() {
        return customerMapper.selectAll();
    }

    @Override
    public PageInfo<Customer> query(QueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize(),"input_time desc"); //对下一句sql进行自动分页
        List<Customer> customers = customerMapper.selectForList(qo); //里面不需要自己写limit
        return new PageInfo<Customer>(customers);
    }

    @Override
    public Customer selectTel(String tel) {
        return customerMapper.selectTel(tel);
    }

    @Override
    public List<Customer> selectStatus() {
        return customerMapper.selectStatus();
    }

    @Override
    public void updateStatus(Long customerId, Integer status) {
        customerMapper.updateStatus(customerId,status);
    }

    @Override
    public List<Customer> selectStatu(int statusCommon) {
        return customerMapper.selectStatu(statusCommon);
    }

    @Override
    public List<Customer> selectStatuAll() {
        return customerMapper.selectStatuAll();
    }

}
