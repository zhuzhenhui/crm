package cn.wolfcode.mapper;

import cn.wolfcode.domain.Employee;
import cn.wolfcode.qo.QueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeMapper {
    int deleteByPrimaryKey(Long id);
    int insert(Employee record);
    Employee selectByPrimaryKey(Long id);
    List<Employee> selectAll();
    int updateByPrimaryKey(Employee record);
    List<Employee> selectForList(QueryObject qo);
    void insertRelation(@Param("eid") Long eid, @Param("rid") Long rid);
    void insertRelation2(@Param("eid") Long eid, @Param("ids") Long[] ids);
    void deleteRelation(Long eid);//删除多表关联的数据
    Employee selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
    void updatePassword(@Param("id") Long id, @Param("newPassword") String newPassword);

    void batchDelete(Long[] ids);

    Employee selectName(String name);

    List<Employee> selectByRoleSn(String[] sns);
}