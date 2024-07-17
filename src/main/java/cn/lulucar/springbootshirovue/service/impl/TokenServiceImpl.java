package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.dto.session.SessionUserInfo;
import cn.lulucar.springbootshirovue.mapper.LoginMapper;
import cn.lulucar.springbootshirovue.service.TokenService;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author wenxiaolan
 * @ClassName TokenServiceImpl
 * @date 2024/7/17 22:04
 * @description
 */
@Service
@Slf4j
public class TokenServiceImpl implements TokenService {
    private final LoginMapper loginMapper;
    private final Cache<String, SessionUserInfo> cacheMap;
    public TokenServiceImpl(LoginMapper loginMapper, Cache<String, SessionUserInfo> cacheMap) {
        this.loginMapper = loginMapper;
        this.cacheMap = cacheMap;
    }

    /**
     * 用户登录验证通过后，生成 token，记录用户已登录的状态。
     * @param username 用户名
     * @return token
     */
    @Override
    public String generateToken(String username) {
        MDC.put("username",username);
        String token = UUID.randomUUID().toString().replace("-", "").substring(0,20);
        // 设置用户信息缓存
        setCache(token,username);
        return token;
    }

    /**
     * 设置用户缓存信息
     * @param token 用户令牌
     * @param username 用户名
     */
    private void setCache(String token, String username) {
        SessionUserInfo info = getUserInfoByUsername(username);
        log.info("设置用户信息缓存:token={} , username={}, info={}", token, username, info);
        cacheMap.put(token,info);
    }

    /**
     * 查询用户的会话信息
     * 如果是管理员，就获取所有菜单和权限
     * @param username 用户名
     * @return 用户会话
     */
    private SessionUserInfo getUserInfoByUsername(String username) {
        SessionUserInfo userInfo = loginMapper.getUserInfo(username);
        // 判断用户的角色
        if (userInfo.getRoleIds().contains(1)) {
            // 管理员
            userInfo.setMenuList(loginMapper.getAllMenu());
            userInfo.setPermissionList(loginMapper.getAllPermissionCode());
        }
        return userInfo;
    }
}
