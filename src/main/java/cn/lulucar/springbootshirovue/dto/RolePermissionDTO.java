package cn.lulucar.springbootshirovue.dto;

import lombok.Data;

import java.util.Set;

/**
 * @author wenxiaolan
 * @ClassName RolePermissionDTO
 * @date 2024/6/9 21:01
 * @description
 */
@Data
public class RolePermissionDTO {
    private Integer roleId;
    private String roleName;
    
    private Set<Integer> permissions;
}
