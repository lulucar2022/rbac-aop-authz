package cn.lulucar.springbootshirovue.dto;


import lombok.Data;

import java.util.List;

/**
 * @author wenxiaolan
 * @ClassName UserAndRoleDTO
 * @date 2024/7/21 23:06
 * @description
 */
@Data
public class UserForAddDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 角色id
     */
    private List<Integer> roleIds;
    
    
    
    
}
