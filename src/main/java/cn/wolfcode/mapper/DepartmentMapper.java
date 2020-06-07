package cn.wolfcode.mapper;

import cn.wolfcode.domain.Department;
import cn.wolfcode.qo.QueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentMapper {

    //添加部门
    void insert(@Param("name") String name, @Param("sn") String sn);
    //删除
    void delete(long id);
    //修改
    void update(@Param("id") long id, @Param("name") String name, @Param("sn") String sn);
    //查找
    List<Department> selectAll();
    //通过id 查询
    Department selectOne(long id);
    //显示的数据量
    //分页查询的结果
    List<Department> selectForList(QueryObject qo);
}
