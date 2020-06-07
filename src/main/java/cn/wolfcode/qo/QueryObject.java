package cn.wolfcode.qo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QueryObject {
    private int currentPage = 1;
    private int pageSize = 3;

    private String keyword;// 关键字
}
