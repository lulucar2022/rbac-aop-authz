package cn.lulucar.springbootshirovue.service;

/**
 * @author wenxiaolan
 * @ClassName TokenService
 * @date 2024/7/15 22:23
 * @description
 */

public interface TokenService {
    
    public String generateToken(String username);
}
