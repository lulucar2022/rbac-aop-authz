package cn.lulucar.springbootshirovue.dto;

import lombok.Data;

import java.util.Set;

/**
 * @author wenxiaolan
 * @ClassName UserForUpdateDTO
 * @date 2024/7/25 1:14
 * @description
 */
@Data
public class UserForUpdateDTO {
    private Integer userId;
    private String nickname;
    private String deleteStatus;
    // 角色集合
    private Set<Integer> roleIds;
    
    
}
