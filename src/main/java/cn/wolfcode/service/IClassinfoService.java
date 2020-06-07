package cn.wolfcode.service;

import cn.wolfcode.domain.Classinfo;
import cn.wolfcode.qo.QueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IClassinfoService {

    void save(Classinfo classinfo,Long tid);
    void delete(Long id);
    void update(Classinfo classinfo,Long tid);

    Classinfo get(Long id);
    List<Classinfo> ListAll();
    //分页查询
    PageInfo<Classinfo> query(QueryObject qo);

    Classinfo selectName(String name);
}
