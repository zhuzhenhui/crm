package cn.wolfcode.service;

import cn.wolfcode.domain.Employee;
import cn.wolfcode.qo.QueryObject;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IEmployeeService {

    void save(Employee employee, Long[] ids);
    void delete(Long id);
    void update(Employee employee, Long[] ids);

    Employee get(Long id);
    List<Employee> ListAll();
    //分页查询
    PageInfo<Employee> query(QueryObject qo);
    //登录查询
    Employee login(String username, String password);
    //修改秘密
    void updatePassword(Long id,String newPassword);

    void batchDelete(Long[] ids);

    Employee selectName(String name);

    Workbook exportXls();

    void importXls(MultipartFile file) throws IOException;

    List<Employee> selectByRoleSn(String ...sns);
}
