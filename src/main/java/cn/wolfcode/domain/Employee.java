package cn.wolfcode.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class Employee {
    private Long id;
    @ExcelProperty("姓名")
    private String name;
    private String password;
    @ExcelProperty("邮箱")
    private String email;
    @ExcelProperty("年龄")
    private Integer age;
    private boolean admin;//true false
    //private Long deptId;
    private Department dept;//获取到部门里需要的内容
    private List<Role> roles = new ArrayList<>();//员工拥有的角色
}