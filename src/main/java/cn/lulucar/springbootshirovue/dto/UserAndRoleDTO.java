package cn.lulucar.springbootshirovue.dto;

import cn.lulucar.springbootshirovue.entity.SysRole;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wenxiaolan
 * @ClassName UserAndRoleDTO
 * @date 2024/9/11 19:21
 * @description
 */
@Data
public class UserAndRoleDTO {
    
    private Integer id;
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否有效  1有效  2无效
     */
    private String deleteStatus;

    /**
     * 角色列表 (包含两个参数)
     * 1.角色roleId
     * 2.角色名称roleName
     */
    private List<RoleDTO> roles;
    
    private Long total;
    private Long current;
    private Long size;
}
