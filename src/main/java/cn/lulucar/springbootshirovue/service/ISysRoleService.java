package cn.lulucar.springbootshirovue.service;

import cn.lulucar.springbootshirovue.entity.SysRole;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;

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
    Page<SysRole> listRole(JSONObject role);
    // 新增角色
    void addRole(SysRole role, Collection<Integer> permissions);
    // 修改角色 
    boolean updateRole(SysRole role, Collection<Integer> permissions);
    // 修改角色名称
    boolean updateRoleName(SysRole role);
    // 删除角色
    boolean deleteRole(SysRole role);
    
    // todo 角色列表（包含了所属的用户和拥有的权限）
    JSONObject listAllRoles();
}
