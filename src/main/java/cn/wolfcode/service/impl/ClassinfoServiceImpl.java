package cn.wolfcode.service.impl;

import cn.wolfcode.domain.Classinfo;
import cn.wolfcode.mapper.ClassinfoMapper;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.service.IClassinfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassinfoServiceImpl implements IClassinfoService{
    @Autowired
    private ClassinfoMapper classinfoMapper;

    @Override
    public void save(Classinfo classinfo, Long teacherId) {
        
        classinfoMapper.insert(classinfo);
        //处理中间表的参数
        if(teacherId!=null ) {
            //遍历员工对应多个权限值
           /* for (Long rid : ids) {
                classinfoMapper.insertRelation(classinfo.getId(),rid);
            }*/
            classinfoMapper.insertRelation2(classinfo.getId(),teacherId);
        }
    }

    @Override
    public void delete(Long id) {
        classinfoMapper.deleteByPrimaryKey(id);
        //删除之后再更新
        classinfoMapper.deleteRelation(id);
    }

    @Override
    public void update(Classinfo classinfo,Long teacherId) {
        
        classinfoMapper.updateByPrimaryKey(classinfo);
        //删除之后再更新
        classinfoMapper.deleteRelation(classinfo.getId());
        //处理中间表的参数
        if(teacherId!=null) {
            //遍历员工对应多个权限值
           /* for (Long rid : ids) {
                classinfoMapper.insertRelation(classinfo.getId(),rid);
            }*/
            classinfoMapper.insertRelation2(classinfo.getId(),teacherId);
        }
    }

    @Override
    public Classinfo get(Long id) {
        return classinfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Classinfo> ListAll() {
        return classinfoMapper.selectAll();
    }
    //分页查询
    @Override
    public PageInfo<Classinfo> query(QueryObject qo) {
        //不需要手动执行count语句,由分页插件来执行
        //告诉分页插件, 当前页以及每页显示的数据就可以了,分页插件会自动算出来start的值, 放到limit
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());//会对下一句sql自动分页  顺序也不能换
        List<Classinfo> classinfos = classinfoMapper.selectForList(qo);//里面不需要自己写limit
        return new PageInfo<Classinfo>(classinfos);
    }

    @Override
    public Classinfo selectName(String name) {
        return classinfoMapper.selectName(name);
    }
}
