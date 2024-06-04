package cn.lulucar.springbootshirovue.service;

import cn.lulucar.springbootshirovue.entity.SysRole;
import cn.lulucar.springbootshirovue.entity.SysUser;
import cn.lulucar.springbootshirovue.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

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
    boolean batchAddUserRole(List<SysRole> roles);
    // 移除用户的所有角色
    boolean removeAllUserRole(Integer userId);
}
