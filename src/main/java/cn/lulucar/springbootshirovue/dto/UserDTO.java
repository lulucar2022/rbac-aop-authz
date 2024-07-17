package cn.lulucar.springbootshirovue.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * @author wenxiaolan
 * @ClassName UserDTO
 * @date 2024/7/15 22:32
 * @description
 */
@Data
public class UserDTO {
    
    private String username;
    private String password;
}
