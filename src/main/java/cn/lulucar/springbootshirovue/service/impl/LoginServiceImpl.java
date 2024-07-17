package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.config.exception.CommonJsonException;
import cn.lulucar.springbootshirovue.entity.SysUser;
import cn.lulucar.springbootshirovue.service.ISysUserService;
import cn.lulucar.springbootshirovue.service.LoginService;
import cn.lulucar.springbootshirovue.service.TokenService;
import cn.lulucar.springbootshirovue.util.CommonUtil;
import cn.lulucar.springbootshirovue.util.PasswordUtil;
import cn.lulucar.springbootshirovue.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author wenxiaolan
 * @ClassName LoginServiceImpl
 * @date 2024/7/16 22:00
 * @description
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    private final ISysUserService iSysUserService;
    private final TokenService tokenService;
    public LoginServiceImpl(ISysUserService iSysUserService, TokenService tokenService) {
        this.iSysUserService = iSysUserService;
        this.tokenService = tokenService;
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 
     */
    @Override
    public JSONObject authLogin(String username, String password) {
        JSONObject info = new JSONObject();
        // 查询用户
        SysUser user = iSysUserService.checkUser(username);
        // 判断用户是否为空
        if (Objects.isNull(user)){
            throw new CommonJsonException(ErrorEnum.E_10010);
        }
        // 验证用户密码
        boolean verified = PasswordUtil.verifyPassword(password, user.getPassword());
        if (!verified) {
            throw new CommonJsonException(ErrorEnum.E_10010);
        }
        // todo 生成 token 的方法
        String token = tokenService.generateToken(user.getUsername());
        info.put("token",token);
        return CommonUtil.successJSON(info);
    }

    /**
     * @return
     */
    @Override
    public JSONObject info() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public JSONObject logout() {
        return null;
    }
}
