package cn.wolfcode.qo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeeQueryObject extends QueryObject {
    private Long deptId = -1L;// 部门id
    private Long teacherId = -1L;// 老師的id
}
