package cn.wolfcode.service.impl;

import cn.wolfcode.domain.Employee;
import cn.wolfcode.mapper.EmployeeMapper;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.service.IEmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@Service
public class EmployeeServiceImpl implements IEmployeeService{
    @Autowired
    private EmployeeMapper employeeMapper;
    @Override
    public void save(Employee employee, Long[] ids) {
        //加密秘密 用户名是盐
        Md5Hash hash = new Md5Hash(employee.getPassword(),employee.getName());
        employee.setPassword(hash.toString());

        employeeMapper.insert(employee);
        //处理中间表的参数
        if(ids!=null && ids.length > 0) {
            //遍历员工对应多个权限值
           /* for (Long rid : ids) {
                employeeMapper.insertRelation(employee.getId(),rid);
            }*/
            employeeMapper.insertRelation2(employee.getId(),ids);
        }
    }

    @Override
    public void delete(Long id) {
        employeeMapper.deleteByPrimaryKey(id);
        employeeMapper.deleteRelation(id);
    }

    @Override
    public void update(Employee employee, Long[] ids) {
        employeeMapper.updateByPrimaryKey(employee);
        //删除之后再更新
        employeeMapper.deleteRelation(employee.getId());
        //处理中间表的参数
        if(ids!=null && ids.length > 0) {
            //遍历员工对应多个权限值
           /* for (Long rid : ids) {
                employeeMapper.insertRelation(employee.getId(),rid);
            }*/
            employeeMapper.insertRelation2(employee.getId(),ids);
        }
    }

    @Override
    public Employee get(Long id) {
        return employeeMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Employee> ListAll() {
        return employeeMapper.selectAll();
    }

    @Override
    public PageInfo<Employee> query(QueryObject qo) {
        //调用Mapper接口实现类对象的方法查询数据,封装 成PageResult 对象返回
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List<Employee> employees = employeeMapper.selectForList(qo);
        return new PageInfo<Employee>(employees);
    }

    @Override
    public Employee login(String username, String password) {
        //查询数据中的用户和密码
        Employee employee = employeeMapper.selectByUsernameAndPassword(username,password);
        //没有该用户提示
        if (employee==null) {
            throw new RuntimeException("账户或密码错误");
        }
        return employee;
    }

    @Override
    public void updatePassword(Long id,String newPassword) {
        //修改密码
        employeeMapper.updatePassword(id,newPassword);

    }

    @Override
    public void batchDelete(Long[] ids) {
        //批量删除
        employeeMapper.batchDelete(ids);
    }

    @Override
    public Employee selectName(String name) {
        return employeeMapper.selectName(name);
    }

    @Override
    public Workbook exportXls() {
        //查询所有的员工
        List<Employee> employees = employeeMapper.selectAll();
        //创建工作簿excel
        Workbook wb = new HSSFWorkbook();
        //创建一张表
        Sheet sheet = wb.createSheet("员工通讯录");
        //创建标题
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("姓名");
        row.createCell(1).setCellValue("邮箱");
        row.createCell(2).setCellValue("年龄");
        for (int i = 0; i <employees.size() ; i++) {
            Employee employee = employees.get(i);
            //创建一行(行的索引 从0开始)
            row = sheet.createRow(i+1);
            //创建单元格(列的索引) 写入内容
            row.createCell(0).setCellValue(employee.getName());
            row.createCell(1).setCellValue(employee.getEmail());
            row.createCell(2).setCellValue(employee.getAge());
        }
        return wb;
    }

    @Override
    public void importXls(MultipartFile file) throws IOException {
        //把接收到的文件以excel 的方式去读取并操作
        Workbook wb = new HSSFWorkbook(file.getInputStream());
        //读取第一页
        Sheet sheet = wb.getSheetAt(0);
        //获取最后一行的索引
        int lastRowNum = sheet.getLastRowNum();
        //标题行 0 不读
        for (int i = 1; i <=lastRowNum ; i++) {
            //获取行数据
            Row row = sheet.getRow(i);
            Employee employee = new Employee();
            String name = row.getCell(0).getStringCellValue();
            employee.setName(name);
            String email = row.getCell(1).getStringCellValue();
            employee.setEmail(email);
            //获取单元格的格式
            /*CellType cellType = row.getCell(2).getCellType();
            System.out.println(cellType);
            if(cellType.equals(CellType.STRING)) {

                String age = row.getCell(2).getStringCellValue();
                employee.setAge(Integer.valueOf(age));
            } else {
                double value = row.getCell(2).getNumericCellValue();
                employee.setAge((int)value);
            }*/
            //设置默认密码
            employee.setPassword("1");
            //保存到数据库
            this.save(employee,null);
        }
    }

    @Override
    public List<Employee> selectByRoleSn(String ...sns) {
        return employeeMapper.selectByRoleSn(sns);
    }
}
