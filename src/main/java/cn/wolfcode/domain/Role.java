package cn.wolfcode.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Role {
    private Long id;

    private String name;

    private String sn;
    private List<Permission> permissions = new ArrayList<>();//角色拥有的权限
}