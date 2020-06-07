package cn.wolfcode.qo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemDictionaryItemQueryObject extends QueryObject {
    //目录的id
    private Long parentId = -1L;
}
