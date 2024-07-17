package cn.lulucar.springbootshirovue.controller;

import cn.lulucar.springbootshirovue.entity.SysUser;
import cn.lulucar.springbootshirovue.service.ISysUserService;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    
    private final ISysUserService iSysUserService;

    public UserController(ISysUserService iSysUserService) {
        this.iSysUserService = iSysUserService;
    }

    //todo 查询用户列表
    @GetMapping({"/",""})
    public JSONObject list(HttpServletRequest request){
        
        return new JSONObject();
        
    }
    //todo 新增用户
    @PostMapping("/")
    public JSONObject add(@RequestBody SysUser user) {
        return new JSONObject();
    }
    //todo 更新用户
    
    //todo 删除用户
}
