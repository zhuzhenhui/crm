package cn.wolfcode.mapper;

import cn.wolfcode.domain.Classinfo;
import cn.wolfcode.qo.QueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClassinfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Classinfo record);

    Classinfo selectByPrimaryKey(Long id);

    List<Classinfo> selectAll();

    int updateByPrimaryKey(Classinfo record);

    List<Classinfo> selectForList(QueryObject qo);

    Classinfo selectName(String name);
    void insertRelation2(@Param("cid") Long cid, @Param("tid") Long teacherId);

    void deleteRelation(Long id);
}