package cn.wolfcode.service;

import cn.wolfcode.domain.Customer;
import cn.wolfcode.qo.QueryObject;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ICustomerService {
    void save(Customer customer);
    void delete(Long id);
    void update(Customer customer);
    Customer get(Long id);
    List<Customer> listAll();
    // 分页查询的方法
    PageInfo<Customer> query(QueryObject qo);

    Customer selectTel(String tel);

    List<Customer> selectStatus();

    void updateStatus(Long customerId, Integer status);

    List<Customer> selectStatu(int statusCommon);

    List<Customer> selectStatuAll();
}
