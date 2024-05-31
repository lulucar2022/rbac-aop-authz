package cn.lulucar.springbootshirovue.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wenxiaolan
 * @ClassName LoginController
 * @date 2024/5/31 17:50
 * @description
 */
@RestController
@Slf4j
@RequestMapping("/auth")
public class LoginController {
    
    //todo 登录
    @PostMapping("/login")
    public JSONObject login() {
        
        return new JSONObject();
    }
    //todo 查询
    @PostMapping("/info")
    public JSONObject info() {
        
        return new JSONObject();
    }
    //todo 登出
    @PostMapping("/logout")
    public JSONObject logout() {
        
        return new JSONObject();
    }
}
