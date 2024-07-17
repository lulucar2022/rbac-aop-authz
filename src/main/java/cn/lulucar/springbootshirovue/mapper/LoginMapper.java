package cn.lulucar.springbootshirovue.mapper;

import cn.lulucar.springbootshirovue.dto.session.SessionUserInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wenxiaolan
 * @ClassName LoginMapper
 * @date 2024/7/17 23:31
 * @description 用户登录需要的方法
 */
public interface LoginMapper {
    // 获取用户会话信息
    SessionUserInfo getUserInfo(String username);

    // 所有菜单
    @Select("select distinct(menu_code) from sys_permission")
    List<String> getAllMenu();
    
    // 所有权限
    @Select("select distinct(permission_code) from sys_permission")
    List<String> getAllPermissionCode();
}
