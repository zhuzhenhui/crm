package cn.wolfcode.service;

import cn.wolfcode.domain.Role;
import cn.wolfcode.qo.QueryObject;
import com.github.pagehelper.PageInfo;
import java.util.List;

public interface IRoleService {

    //分页查询
    PageInfo<Role> query(QueryObject qo);
    void delete(Long id);
    List<Role> listAll();

    Role get(Long id);

    void update(Role role, Long[] ids);

    void save(Role role, Long[] ids);
}
