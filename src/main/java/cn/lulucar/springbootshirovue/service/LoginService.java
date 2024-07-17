package cn.lulucar.springbootshirovue.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author wenxiaolan
 * @ClassName LoginService
 * @date 2024/7/15 22:23
 * @description
 */
public interface LoginService {
    // 登录表单提交
    JSONObject authLogin(String username, String password);
    // 查询当前登录用户的权限等信息
    JSONObject info();
    // 登出
    JSONObject logout();
}
