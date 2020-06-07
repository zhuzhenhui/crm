package cn.wolfcode.service.impl;

import cn.wolfcode.domain.Department;
import cn.wolfcode.mapper.DepartmentMapper;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.service.IDepartmentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DepartmentServiceImpl implements IDepartmentService{
    @Autowired
    private DepartmentMapper departmentMapper;
    @Override
    public void save(Department d) {
        departmentMapper.insert(d.getName(),d.getSn());
    }

    @Override
    public void delete(Long id) {
        departmentMapper.delete(id);
    }

    @Override
    public void update(Department d) {
        departmentMapper.update(d.getId(),d.getName(),d.getSn());
    }

    @Override
    public Department get(Long id) {
        return departmentMapper.selectOne(id);
    }

    @Override
    public List<Department> ListAll() {
        return departmentMapper.selectAll();
    }
    //分页查询
    @Override
    public PageInfo<Department> query(QueryObject qo) {
        //不需要手动执行count语句,由分页插件来执行
        //告诉分页插件, 当前页以及每页显示的数据就可以了,分页插件会自动算出来start的值, 放到limit
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());//会对下一句sql自动分页  顺序也不能换
        List<Department> departments = departmentMapper.selectForList(qo);//里面不需要自己写limit
        return new PageInfo<Department>(departments);
    }
}
