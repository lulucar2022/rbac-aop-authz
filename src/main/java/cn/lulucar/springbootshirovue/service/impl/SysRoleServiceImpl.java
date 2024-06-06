package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.entity.SysPermission;
import cn.lulucar.springbootshirovue.entity.SysRole;
import cn.lulucar.springbootshirovue.entity.SysRolePermission;
import cn.lulucar.springbootshirovue.mapper.SysRoleMapper;
import cn.lulucar.springbootshirovue.service.ISysRolePermissionService;
import cn.lulucar.springbootshirovue.service.ISysRoleService;
import cn.lulucar.springbootshirovue.util.PageFromRequestUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 后台角色表 服务实现类
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final SysRoleMapper sysRoleMapper;
    @Autowired
    private ISysRolePermissionService iSysRolePermissionService;

    public SysRoleServiceImpl(SysRoleMapper sysRoleMapper) {
        this.sysRoleMapper = sysRoleMapper;
    }

    /**
     * 角色列表
     * @param role Map结构
     * @return 
     */
    @Override
    public Page<SysRole> listRole(JSONObject role) {
        
        Page<SysRole> page = new Page<>((Long) role.get("current"), (Long) role.get("size"));
        return sysRoleMapper.selectPage(page,null);
    }

    /**
     * 新增角色
     * 1.新增角色
     * 2.新增角色的权限    
     * @param jsonObject 包含 角色实体和权限列表
     * @return
     */
    @Override
    public boolean addRole(JSONObject jsonObject) {
        
        
        return false;
    }

    /**
     * 修改角色
     * 1.修改角色名称
     * 2.移除角色旧权限
     * 3.给角色添加新权限
     * @param role 角色实体
     * @param permissions 新权限列表
     */
    @Override
    public boolean updateRole(SysRole role, Collection<Integer> permissions) {
        int roleId = role.getId();
        // 新权限列表
        List<Integer> newPerms = (List<Integer>) permissions;
        // 现有权限列表
        List<SysRolePermission> roleAllPermissions = iSysRolePermissionService.getRoleAllPermissions(roleId);
        List<Integer> oldPerms = new ArrayList<>();
        for (SysRolePermission roleAllPermission : roleAllPermissions) {
            oldPerms.add(roleAllPermission.getPermissionId());
        }
        
        
        return false;
    }

    /**
     * 修改角色名称
     * 该方法为修改角色调用
     * @param role 角色实体
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRoleName(SysRole role) {
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRole::getRoleName,role.getRoleName());
        // 判断是否存在同名角色
        Long existName = sysRoleMapper.selectCount(lambdaQueryWrapper);
        
        sysRoleMapper.updateById(role);
        return existName == 0L;
    }

    /**
     * @param role 角色实体
     * @return
     */
    @Override
    public boolean deleteRole(SysRole role) {
        return false;
    }
}
