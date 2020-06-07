package cn.wolfcode.service;

import cn.wolfcode.domain.Permission;
import cn.wolfcode.qo.QueryObject;
import com.github.pagehelper.PageInfo;
import java.util.List;
public interface IPermissionService {
    void delete(Long id);
    Permission get(Long id);
    List<Permission> ListAll();
    //分页查询
    PageInfo<Permission> query(QueryObject qo);

    void reload();

    List<String> selectExpressionByEmpId(Long id);
}
