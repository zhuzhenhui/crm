package cn.wolfcode.service.impl;

import cn.wolfcode.domain.SystemDictionaryItem;
import cn.wolfcode.mapper.SystemDictionaryItemMapper;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.service.ISystemDictionaryItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemDictionaryItemServiceImpl implements ISystemDictionaryItemService{
    @Autowired
    private SystemDictionaryItemMapper systemDictionaryItemMapper;
    @Override
    public void save(SystemDictionaryItem systemDictionaryItem) {

        systemDictionaryItemMapper.insert(systemDictionaryItem);
    }

    @Override
    public void delete(Long id) {
        systemDictionaryItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(SystemDictionaryItem systemDictionaryItem) {
        systemDictionaryItemMapper.updateByPrimaryKey(systemDictionaryItem);
    }

    @Override
    public SystemDictionaryItem get(Long id) {
        return systemDictionaryItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SystemDictionaryItem> ListAll() {
        return systemDictionaryItemMapper.selectAll();
    }
    //分页查询
    @Override
    public PageInfo<SystemDictionaryItem> query(QueryObject qo) {
        //不需要手动执行count语句,由分页插件来执行
        //告诉分页插件, 当前页以及每页显示的数据就可以了,分页插件会自动算出来start的值, 放到limit
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());//会对下一句sql自动分页  顺序也不能换
        List<SystemDictionaryItem> systemDictionaryItems = systemDictionaryItemMapper.selectForList(qo);//里面不需要自己写limit
        return new PageInfo<SystemDictionaryItem>(systemDictionaryItems);
    }

    @Override
    public List<SystemDictionaryItem> selectParentId(Long parentId) {
        return systemDictionaryItemMapper.selectParentId(parentId);
    }

    @Override
    public List<SystemDictionaryItem> selectByParentSn(String sn) {
        return systemDictionaryItemMapper.selectByParentSn(sn);
    }
}
