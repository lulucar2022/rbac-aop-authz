package cn.lulucar.springbootshirovue.controller;

import cn.lulucar.springbootshirovue.dto.UserDTO;
import cn.lulucar.springbootshirovue.service.LoginService;
import cn.lulucar.springbootshirovue.util.CommonUtil;
import cn.lulucar.springbootshirovue.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wenxiaolan
 * @ClassName LoginController
 * @date 2024/5/31 17:50
 * @description
 */
@RestController
@Slf4j
@RequestMapping("/login")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // 登录
    @PostMapping({"", "/"})
    public JSONObject authLogin(@RequestBody UserDTO userDTO) {
        if (userDTO.getUsername().isEmpty()|| userDTO.getPassword().isEmpty()) {
            return CommonUtil.errorJSON(ErrorEnum.E_90003);
        }
        return loginService.authLogin(userDTO.getUsername(),userDTO.getPassword());
    }
    // 查询
    @GetMapping({"/","/info"})
    public JSONObject info() {
        return loginService.info();
    }
    // 登出
    @PostMapping("/logout")
    public JSONObject logout() {
        return loginService.logout();
    }
}
