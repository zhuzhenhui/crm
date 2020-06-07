package cn.wolfcode.service.impl;

import cn.wolfcode.domain.Courseorder;
import cn.wolfcode.domain.Customer;
import cn.wolfcode.mapper.CourseorderMapper;
import cn.wolfcode.mapper.CustomerMapper;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.service.ICourseorderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CourseorderServiceImpl implements ICourseorderService {

    @Autowired
    private CourseorderMapper courseorderMapper;

    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public void save(Courseorder courseorder) {
        //添加完之后还需要设置为正式客户
        Long id = courseorder.getCustomer().getId();
        customerMapper.updateStatus(id, Customer.STATUS_NORMAL);
        //添加时间
        courseorder.setInputTime(new Date());
        courseorderMapper.insert(courseorder);
    }

    @Override
    public void delete(Long id) {
        courseorderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Courseorder courseorder) {

        //添加时间
        courseorder.setInputTime(new Date());
        courseorderMapper.updateByPrimaryKey(courseorder);
    }

    @Override
    public Courseorder get(Long id) {
        return courseorderMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Courseorder> listAll() {
        return courseorderMapper.selectAll();
    }

    @Override
    public PageInfo<Courseorder> query(QueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize()); //对下一句sql进行自动分页
        List<Courseorder> courseorders = courseorderMapper.selectForList(qo); //里面不需要自己写limit
        return new PageInfo<Courseorder>(courseorders);
    }
}
