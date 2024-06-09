package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.config.exception.ParameterFormatException;
import cn.lulucar.springbootshirovue.entity.SysRole;
import cn.lulucar.springbootshirovue.entity.SysRolePermission;
import cn.lulucar.springbootshirovue.mapper.SysRoleMapper;
import cn.lulucar.springbootshirovue.service.ISysRolePermissionService;
import cn.lulucar.springbootshirovue.service.ISysRoleService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * <p>
 * 后台角色表 服务实现类
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
@Service
@Slf4j
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final SysRoleMapper sysRoleMapper;
    private final ISysRolePermissionService iSysRolePermissionService;

    public SysRoleServiceImpl(SysRoleMapper sysRoleMapper, ISysRolePermissionService iSysRolePermissionService) {
        this.sysRoleMapper = sysRoleMapper;
        this.iSysRolePermissionService = iSysRolePermissionService;
    }

    /**
     * 角色列表
     * @param role Map结构
     * @return 角色分页列表
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
     * @param role 角色实体
     * @param permissions 权限列表
     * @return true为成功插入
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addRole(SysRole role, Collection<Integer> permissions) {
        if (ObjectUtils.isEmpty(role) || role.getRoleName().isEmpty()){
            throw new ParameterFormatException("role实体缺少必填属性");
        }
        if (ObjectUtils.isEmpty(permissions)) {
            throw new ParameterFormatException("permissions列表缺少值或者为空");
        }
        // 新增角色
        int inserted = sysRoleMapper.insert(role);
        // 拿到roleId
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRole::getRoleName, role.getRoleName());
        SysRole roleTemp = sysRoleMapper.selectOne(lambdaQueryWrapper);
        // 新增角色的权限
        boolean b = iSysRolePermissionService.insertRolePermission(roleTemp.getId(), permissions);

        return b && inserted > 0;
    }

    /**
     * 修改角色
     * 1.修改角色名称
     * 2.移除角色旧权限
     * 3.给角色添加新权限
     * @param role 角色实体
     * @param permissions 新权限列表
     * @return true为成功修改
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRole(SysRole role, Collection<Integer> permissions) {
        int roleId = role.getId();
        // 新权限列表
        List<Integer> newPerms = new ArrayList<>(permissions) ;
        log.info("新权限：{}",newPerms.toArray());
        // 现有权限列表
        List<SysRolePermission> roleAllPermissions = iSysRolePermissionService.getRoleAllPermissions(roleId);
        Set<Integer> oldPerms = new HashSet<>();
        for (SysRolePermission roleAllPermission : roleAllPermissions) {
            oldPerms.add(roleAllPermission.getPermissionId());
        }
        log.info("现有权限：{}",oldPerms.toArray());
        // 修改角色名称
        boolean b = updateRoleName(role);
        // 添加新的权限
        boolean b1 = iSysRolePermissionService.insertRolePermission(roleId, newPerms);
        // 移除旧的权限
        boolean b2 = iSysRolePermissionService.removeRolePermission(roleId, oldPerms);
        return b && b1 && b2;
    }

    /**
     * 修改角色名称
     * 该方法为修改角色方法调用
     * @param role 角色实体
     * @return true 为成功修改
     */
    
    @Override
    public boolean updateRoleName(SysRole role) {
        if (role == null || role.getId() == null || role.getRoleName().isEmpty()) {
            throw new ParameterFormatException("role实体缺少必要属性");
        }
        LambdaUpdateWrapper<SysRole> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysRole::getId,role.getId());

        int updated = sysRoleMapper.update(lambdaUpdateWrapper);
        return updated > 0;
    }

    /**
     * 删除角色
     * 1.先判断角色是否还被用户绑定
     * 2.再删除角色的权限
     * 3.最后删除角色
     * @param role 角色实体
     * @return true 为成功删除
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteRole(SysRole role) {
        if (ObjectUtils.isEmpty(role) || role.getId() == null) {
            throw new ParameterFormatException("role实体参数异常");
        }
        // 判断角色是否被用户使用
        int deleted = sysRoleMapper.deleteById(role);
        return deleted > 0;
    }
}
