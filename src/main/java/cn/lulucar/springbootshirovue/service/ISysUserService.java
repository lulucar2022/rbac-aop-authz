package cn.lulucar.springbootshirovue.service;

import cn.lulucar.springbootshirovue.dto.UserAndRoleDTO;
import cn.lulucar.springbootshirovue.dto.UserForUpdateDTO;
import cn.lulucar.springbootshirovue.entity.SysUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
public interface ISysUserService extends IService<SysUser> {
    // 查询用户列表及其角色
    List<UserAndRoleDTO> listUserAndRole(Long pageNum, Long pageRow);

    // 用户列表
    Page<SysUser> listUser(Long current, Long size);
    // 查询用户总数
    int countUser();
    // 添加用户
    void addUser(SysUser user, List<Integer> roleIds);

    /**
     * 修改用户，包括修改用户关联的角色
     *
     * @param user 参数：userId，nickname，deleteStatus
     * @param roleIds 参数：roleId 集合
     */
    void updateUser(SysUser user, Set<Integer> roleIds);
    
    // 查询用户
    SysUser checkUser(String username);

    /**
     * 删除用户，根据 userId
     */
    void deleteUser(Integer userId, Set<Integer> roleIds);
    
    
}
