package cn.lulucar.springbootshirovue.service;

import cn.lulucar.springbootshirovue.entity.SysRolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色-权限关联表 服务类
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {
    // 给角色插入权限
    boolean insertRolePermission(int roleId, Collection<Integer> permissions);
    // 根据角色id查询权限
    List<SysRolePermission> getRoleAllPermissions(int roleId);
}
