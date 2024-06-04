package cn.lulucar.springbootshirovue.service;

import cn.lulucar.springbootshirovue.entity.SysRole;
import cn.lulucar.springbootshirovue.entity.SysUser;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
public interface ISysUserService extends IService<SysUser> {
    // 用户列表
    Page<SysUser> listUser(JSONObject user);
    // 查询用户总数
    int countUser();
    // 添加用户
    boolean addUser(SysUser user);

    /**
     * 修改用户，包括删除所有角色和添加新角色
     * @param user
     * @return
     */
    boolean updateUser(SysUser user);
    
    // 登录时参数验证
    
}
