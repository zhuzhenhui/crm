package cn.wolfcode.mapper;

import cn.wolfcode.domain.Role;
import cn.wolfcode.qo.QueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    Role selectByPrimaryKey(Long id);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);
    List<Role> selectForList(QueryObject qo);
    void insertRelation2(@Param("rid") Long rid, @Param("ids") Long[] ids);
    void deleteRelation(Long rid);

    List<String> selectSnByEmpId(Long id);
}