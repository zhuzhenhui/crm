package cn.wolfcode.service;

import cn.wolfcode.domain.Department;
import cn.wolfcode.qo.QueryObject;
import com.github.pagehelper.PageInfo;
import java.util.List;

public interface IDepartmentService {

    void save(Department d);
    void delete(Long id);
    void update(Department d);

    Department get(Long id);
    List<Department> ListAll();
    //分页查询
    PageInfo<Department> query(QueryObject qo);

}
