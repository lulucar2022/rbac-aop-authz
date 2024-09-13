package cn.lulucar.springbootshirovue.service;

import cn.lulucar.springbootshirovue.dto.RoleDTO;
import cn.lulucar.springbootshirovue.dto.RoleMenuPermissionUserDTO;
import cn.lulucar.springbootshirovue.entity.SysRole;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 后台角色表 服务类
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
public interface ISysRoleService extends IService<SysRole> {
    // 所有角色
    List<RoleDTO> getAllRoles();
    // 新增角色
    void addRole(SysRole role, Collection<Integer> permissions);
    // 修改角色 
    void updateRole(SysRole role, Collection<Integer> permissions);
    // 修改角色名称
    boolean updateRoleName(SysRole role);
    // 删除角色
    void deleteRole(SysRole role);
    // 角色列表（包含了所属的用户和拥有的权限）
    List<RoleMenuPermissionUserDTO> listAllRoles();
    // 查询角色根据角色id（roleId）
    SysRole getRoleById(Integer roleId);
}
