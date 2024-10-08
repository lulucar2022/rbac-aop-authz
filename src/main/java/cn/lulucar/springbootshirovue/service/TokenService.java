package cn.lulucar.springbootshirovue.service;

import cn.lulucar.springbootshirovue.dto.session.SessionUserInfo;

/**
 * @author wenxiaolan
 * @ClassName TokenService
 * @date 2024/7/15 22:23
 * @description
 */

public interface TokenService {
    
    public String generateToken(String username);

    SessionUserInfo getUserInfo();

    void invalidateToken();
}
