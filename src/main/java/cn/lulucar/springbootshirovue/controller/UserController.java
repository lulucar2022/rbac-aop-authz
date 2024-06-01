package cn.lulucar.springbootshirovue.controller;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wenxiaolan
 * @ClassName UserController
 * @date 2024/5/31 17:51
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    
    //todo 查询用户列表
    @GetMapping({"/",""})
    public JSONObject list(HttpServletRequest request){
        
        return new JSONObject();
        
    }
    //todo 新增用户
    
    //todo 更新用户
    
    //todo 
}
