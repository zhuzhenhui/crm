package cn.wolfcode.service;

import cn.wolfcode.domain.SystemDictionaryItem;
import cn.wolfcode.qo.QueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ISystemDictionaryItemService {

    void save(SystemDictionaryItem systemDictionaryItem);
    void delete(Long id);
    void update(SystemDictionaryItem systemDictionaryItem);

    SystemDictionaryItem get(Long id);
    List<SystemDictionaryItem> ListAll();
    //分页查询
    PageInfo<SystemDictionaryItem> query(QueryObject qo);

    List<SystemDictionaryItem> selectParentId(Long parentId);

    List<SystemDictionaryItem> selectByParentSn(String sn);
}
