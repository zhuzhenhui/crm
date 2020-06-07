package cn.wolfcode.qo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseorderQueryObject extends QueryObject {

    //销售课程的id
    private Long courseId = -1L;
}
