package cn.lulucar.springbootshirovue.dto;

import lombok.Data;

import java.util.List;

/**
 * @author wenxiaolan
 * @ClassName RoleMenuPermissionUserDTO
 * @date 2024/9/12 18:00
 * @description
 */
@Data
public class RoleMenuPermissionUserDTO {
    private Integer roleId;
    private String roleName;
    private List<UserIdNameDTO> users;
    private List<RoleMenu> menus;
    @Data
    public static class UserIdNameDTO{
        private Integer userId;
        private String nickname;
        
    }
    @Data
    public static class RoleMenu{
        private String menuCode;
        private String menuName;
        private List<Permission> permissions;
        @Data
        public static class Permission {
            private Integer permissionId;
            private String permissionName;
        }
    }
    
}
