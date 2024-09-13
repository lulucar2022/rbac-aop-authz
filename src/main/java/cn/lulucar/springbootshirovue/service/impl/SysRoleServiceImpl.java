package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.config.exception.CommonJsonException;
import cn.lulucar.springbootshirovue.config.exception.ParameterFormatException;
import cn.lulucar.springbootshirovue.dto.RoleDTO;
import cn.lulucar.springbootshirovue.dto.RoleMenuPermissionUserDTO;
import cn.lulucar.springbootshirovue.entity.*;
import cn.lulucar.springbootshirovue.mapper.SysRoleMapper;
import cn.lulucar.springbootshirovue.service.*;
import cn.lulucar.springbootshirovue.util.StringUtil;
import cn.lulucar.springbootshirovue.util.constants.ErrorEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

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
     * 获取所有角色
     * id，roleName
     *
     * @return （id，roleName）的列表
     */
    @Override
    public List<RoleDTO> getAllRoles() {
        return sysRoleMapper.getAllRoles();
    }

    /**
     * 新增角色
     * 1.新增角色
     * 2.新增角色的权限
     *
     * @param role        角色实体
     * @param permissions 权限列表
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addRole(SysRole role, Collection<Integer> permissions) {
        if (ObjectUtils.isEmpty(role) || StringUtil.isNullOrEmpty(role.getRoleName()) || ObjectUtils.isEmpty(permissions)){
            throw new CommonJsonException(ErrorEnum.E_90003);
        }
        // 新增角色
        int inserted = sysRoleMapper.insert(role);
        // 拿到roleId
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRole::getRoleName, role.getRoleName());
        SysRole roleTemp = sysRoleMapper.selectOne(lambdaQueryWrapper);
        // 新增角色的权限
        boolean b = iSysRolePermissionService.insertRolePermission(roleTemp.getId(), permissions);
    }

    /**
     * 修改角色
     * 1.修改角色名称
     * 2.移除角色旧权限
     * 3.给角色添加新权限
     *
     * @param role        角色实体
     * @param permissions 新权限列表
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRole(SysRole role, Collection<Integer> permissions) {
        if (permissions == null || role == null) 
            throw new CommonJsonException(ErrorEnum.E_90003);
        int roleId = role.getId();
        // 新权限列表
        Set<Integer> newPerms = new HashSet<>(permissions) ;
        log.info("新权限：{}",newPerms);
        // 现有权限列表
        Set<SysRolePermission> roleAllPermissions = new HashSet<>(iSysRolePermissionService.getRoleAllPermissions(roleId));
        Set<Integer> oldPerms = new HashSet<>();
        for (SysRolePermission roleAllPermission : roleAllPermissions) {
            oldPerms.add(roleAllPermission.getPermissionId());
        }
        log.info("现有权限：{}",oldPerms);
        
        // 添加权限集合
        Set<Integer> addPerms = newPerms.stream().filter(e -> !oldPerms.contains(e)).collect(Collectors.toSet());
        log.info("需要添加的权限：{}",addPerms);
        // 删除权限集合
        Set<Integer> removePerms = oldPerms.stream().filter(e -> !newPerms.contains(e)).collect(Collectors.toSet());
        log.info("需要删除的权限：{}",removePerms);
        // 修改角色名称
        boolean b = updateRoleName(role);
        // todo 这里的更新逻辑错误，需要修改，应该把传入的权限列表A与现有的权限B做逻辑运算，A-B为新增，B-A为删除
        // 添加新的权限
        boolean b1 = iSysRolePermissionService.insertRolePermission(roleId, addPerms);
        // 移除旧的权限
        boolean b2 = iSysRolePermissionService.removeRolePermission(roleId, removePerms);
    }

    /**
     * 修改角色名称
     * 该方法为修改角色方法调用
     * ！！！ 修改的角色名称不能与现有的名称重复
     * @param role 角色实体
     * @return true 为成功修改
     */
    
    @Override
    public boolean updateRoleName(SysRole role) {
        // 参数校验
        if (role == null || role.getId() == null || StringUtil.isNullOrEmpty(role.getRoleName())) {
            throw new CommonJsonException(ErrorEnum.E_90003);
        }
        // 检查现有角色名称与新名称是否重复
        boolean isExist = false;
        List<RoleDTO> allRoles = sysRoleMapper.getAllRoles();
        for (RoleDTO roleTemp : allRoles) {
            log.info(String.valueOf(roleTemp));
            if (roleTemp.getRoleName().equals(role.getRoleName())) {
                isExist = true;
                break;
            }
        }
        // 执行条件
        LambdaUpdateWrapper<SysRole> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysRole::getId,role.getId());
        
        // 不存在相同名称，执行更新方法。
        if (!isExist) {
            return sysRoleMapper.update(role,lambdaUpdateWrapper) > 0;
        }
        return true;
    }

    /**
     * 删除角色
     * 1.先判断角色是否还被用户绑定
     * 2.再删除角色的权限
     * 3.最后删除角色
     *
     * @param role 角色实体
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRole(SysRole role) {
        if (role == null || role.getId() == null) {
            throw new ParameterFormatException("role实体参数异常");
        }
        // 判断角色是否被用户使用
        int deleted = sysRoleMapper.deleteById(role);
    }

    /**
     * 角色列表（包含了所属的用户和拥有的权限）
     * 因为有循环依赖，所以把这个方法放在了service层的 CoordinatorService中
     * @return 
     */
    @Override
    public List<RoleMenuPermissionUserDTO> listAllRoles() {
        return null;
    }


    /**
     * @param roleId 角色Id 
     * @return 单个角色信息
     */
    @Override
    public SysRole getRoleById(Integer roleId) {
        if (roleId == null) {
            throw new ParameterFormatException("角色Id不能为空");
        }
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRole::getId,roleId);
        return sysRoleMapper.selectOne(lambdaQueryWrapper);
    }
}
