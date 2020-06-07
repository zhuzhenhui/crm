package cn.wolfcode.service.impl;

import cn.wolfcode.domain.SystemDictionary;
import cn.wolfcode.mapper.SystemDictionaryMapper;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.service.ISystemDictionaryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemDictionaryServiceImpl implements ISystemDictionaryService{
    @Autowired
    private SystemDictionaryMapper systemDictionaryMapper;
    @Override
    public void save(SystemDictionary systemDictionary) {
        systemDictionaryMapper.insert(systemDictionary);
    }

    @Override
    public void delete(Long id) {
        systemDictionaryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(SystemDictionary systemDictionary) {
        systemDictionaryMapper.updateByPrimaryKey(systemDictionary);
    }

    @Override
    public SystemDictionary get(Long id) {
        return systemDictionaryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SystemDictionary> ListAll() {
        return systemDictionaryMapper.selectAll();
    }
    //分页查询
    @Override
    public PageInfo<SystemDictionary> query(QueryObject qo) {
        //不需要手动执行count语句,由分页插件来执行
        //告诉分页插件, 当前页以及每页显示的数据就可以了,分页插件会自动算出来start的值, 放到limit
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());//会对下一句sql自动分页  顺序也不能换
        List<SystemDictionary> systemDictionarys = systemDictionaryMapper.selectForList(qo);//里面不需要自己写limit
        return new PageInfo<SystemDictionary>(systemDictionarys);
    }
}
