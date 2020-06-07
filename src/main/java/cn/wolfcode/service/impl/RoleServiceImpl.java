package cn.wolfcode.service.impl;

import cn.wolfcode.domain.Role;
import cn.wolfcode.mapper.RoleMapper;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.service.IRoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public PageInfo<Role> query(QueryObject qo) {
        //调用Mapper接口实现类对象的方法查询数据,封装 成PageResult 对象返回
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List<Role> roles = roleMapper.selectForList(qo);
        return new PageInfo<Role>(roles);
    }

    @Override
    public void delete(Long id) {
        roleMapper.deleteByPrimaryKey(id);
        roleMapper.deleteRelation(id);
    }

    @Override
    public List<Role> listAll() {
        return roleMapper.selectAll();
    }

    @Override
    public Role get(Long id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Role role, Long[] ids) {
        roleMapper.updateByPrimaryKey(role);
        //删除关系
        roleMapper.deleteRelation(role.getId());
        //处理中间表
        if(ids != null && ids.length > 0) {
            roleMapper.insertRelation2(role.getId(),ids);
        }
    }

    @Override
    public void save(Role role, Long[] ids) {
        roleMapper.insert(role);
        //处理中间表
        if(ids != null && ids.length > 0) {
            roleMapper.insertRelation2(role.getId(),ids);
        }
    }
}
