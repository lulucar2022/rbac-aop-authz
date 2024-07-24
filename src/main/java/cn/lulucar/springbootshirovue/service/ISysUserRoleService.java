package cn.lulucar.springbootshirovue.service;

import cn.lulucar.springbootshirovue.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 用户-角色关联表 服务类
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
public interface ISysUserRoleService extends IService<SysUserRole> {
    // 批量添加用户的角色
    void batchAddUserRole(Integer userId, Collection<Integer> roles);
    // 移除用户的所有角色
    void batchRemoveUserRole(Integer userId, Collection<Integer> roles);
    // 根据用户id查询角色
    List<SysUserRole> getUserAllRoles(Integer userId);
}
