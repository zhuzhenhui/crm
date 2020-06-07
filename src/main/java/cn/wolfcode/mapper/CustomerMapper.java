package cn.wolfcode.mapper;

import cn.wolfcode.domain.Customer;
import cn.wolfcode.qo.QueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Customer record);

    Customer selectByPrimaryKey(Long id);

    List<Customer> selectAll();

    int updateByPrimaryKey(Customer record);

    List<Customer> selectForList(QueryObject qo);

    Customer selectTel(String tel);

    List<Customer> selectStatus();

    void updateStatus(@Param("customerId") Long customerId, @Param("status") Integer status);

    List<Customer> selectStatu(int statusCommon);

    List<Customer> selectStatuAll();

    void updates(@Param("sourceId") Long sourceId, @Param("customerId") Long customerId);
}