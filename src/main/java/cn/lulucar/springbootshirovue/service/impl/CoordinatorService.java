package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.dto.RoleMenuPermissionUserDTO;
import cn.lulucar.springbootshirovue.entity.*;
import cn.lulucar.springbootshirovue.mapper.SysRoleMapper;
import cn.lulucar.springbootshirovue.service.ISysPermissionService;
import cn.lulucar.springbootshirovue.service.ISysRolePermissionService;
import cn.lulucar.springbootshirovue.service.ISysUserRoleService;
import cn.lulucar.springbootshirovue.service.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wenxiaolan
 * @ClassName CoordinatorService
 * @date 2024/9/12 20:29
 * @description 协调user和role两个service方法的循环依赖
 */
@Service
@Slf4j
public class CoordinatorService {

    private final SysRoleMapper sysRoleMapper;
    private final ISysRolePermissionService iSysRolePermissionService;
    private final ISysUserRoleService iSysUserRoleService;
    private final ISysUserService iSysUserService;
    private final ISysPermissionService iSysPermissionService;

    public CoordinatorService(SysRoleMapper sysRoleMapper, ISysRolePermissionService iSysRolePermissionService, ISysUserRoleService iSysUserRoleService, ISysUserService iSysUserService, ISysPermissionService iSysPermissionService) {
        this.sysRoleMapper = sysRoleMapper;
        this.iSysRolePermissionService = iSysRolePermissionService;
        this.iSysUserRoleService = iSysUserRoleService;
        this.iSysUserService = iSysUserService;
        this.iSysPermissionService = iSysPermissionService;
    }
    /**
     * 角色列表（包含用户和权限）
     *
     * @return 角色列表（包含所属的用户和用户的权限）
     */
    public List<RoleMenuPermissionUserDTO> listAllRoles() {

        List<RoleMenuPermissionUserDTO> roleMenuPermissionUserDTOS = new ArrayList<>();
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        sysRoles.forEach(sysRole -> {

            // 1. 添加 roleId roleName
            RoleMenuPermissionUserDTO roleMenuPermissionUserDTO =new RoleMenuPermissionUserDTO();
            roleMenuPermissionUserDTO.setRoleId(sysRole.getId());
            roleMenuPermissionUserDTO.setRoleName(sysRole.getRoleName());

            // 2. 添加 user类的 userId和nickname
            LambdaQueryWrapper<SysUserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userRoleLambdaQueryWrapper.eq(SysUserRole::getRoleId,sysRole.getId());
            List<SysUserRole> userRoles = iSysUserRoleService.list(userRoleLambdaQueryWrapper);
            List<RoleMenuPermissionUserDTO.UserIdNameDTO> userIdNameDTOList = new ArrayList<>();
            userRoles.forEach(userRole -> {
                log.info("userRole:{}",userRole);
                // 2.1 查询用户信息
                LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
                sysUserLambdaQueryWrapper.eq(SysUser::getId,userRole.getUserId());
                SysUser user = iSysUserService.getOne(sysUserLambdaQueryWrapper);
                log.info("user:{}",user);   
                // 2.2 创建 user 内部类 并添加信息
                RoleMenuPermissionUserDTO.UserIdNameDTO userIdNameDTO = new RoleMenuPermissionUserDTO.UserIdNameDTO();
                if (user != null) {
                    userIdNameDTO.setUserId(user.getId());
                    userIdNameDTO.setNickname(user.getNickname());
                }
                userIdNameDTOList.add(userIdNameDTO);
            });
            // 2.3 添加到集合
            roleMenuPermissionUserDTO.setUsers(userIdNameDTOList);
            // 3. 添加 perm类的 menuId和menuName
            LambdaQueryWrapper<SysRolePermission> rolePermissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            rolePermissionLambdaQueryWrapper.eq(SysRolePermission::getRoleId,sysRole.getId());
            List<SysRolePermission> rolePermissions = iSysRolePermissionService.list(rolePermissionLambdaQueryWrapper);
            List<RoleMenuPermissionUserDTO.RoleMenu> roleMenuList = new ArrayList<>();
            Map<String, RoleMenuPermissionUserDTO.RoleMenu> menuMap = new HashMap<>();
            rolePermissions.forEach(rolePermission -> {
                // 3.1 查询权限信息
                LambdaQueryWrapper<SysPermission> sysPermissionLambdaQueryWrapper =new LambdaQueryWrapper<>();
                sysPermissionLambdaQueryWrapper.eq(SysPermission::getId,rolePermission.getPermissionId());
                SysPermission sysPermission = iSysPermissionService.getOne(sysPermissionLambdaQueryWrapper);
                // 3.2 创建 perm 内部类 并添加信息
                RoleMenuPermissionUserDTO.RoleMenu roleMenu = new RoleMenuPermissionUserDTO.RoleMenu();
                roleMenu.setMenuCode(sysPermission.getMenuCode());
                roleMenu.setMenuName(sysPermission.getMenuName());
                // 3.3 创建 roleMenu 的 内部类 permission 并添加信息
                RoleMenuPermissionUserDTO.RoleMenu.Permission permission = new RoleMenuPermissionUserDTO.RoleMenu.Permission();
                permission.setPermissionId(sysPermission.getId());
                permission.setPermissionName(sysPermission.getPermissionName());
                // 3.4 判断 menuMap 是否包含菜单 menuCode
                // 3.4.1 同一个 menuCode 下，添加多个 permission 信息
                // 3.4.2 累计到 menuMap 中
                if (menuMap.containsKey(sysPermission.getMenuCode())) {
                    RoleMenuPermissionUserDTO.RoleMenu existingRoleMenu = menuMap.get(sysPermission.getMenuCode());
                    existingRoleMenu.getPermissions().add(permission);
                    menuMap.put(sysPermission.getMenuCode(), existingRoleMenu);
                } else {
                    List<RoleMenuPermissionUserDTO.RoleMenu.Permission> permissions = new ArrayList<>();
                    permissions.add(permission);
                    roleMenu.setPermissions(permissions);
                    menuMap.put(sysPermission.getMenuCode(), roleMenu);
                }
            });
            // 3.4.3 循环一个 roleId，将 menuMap 中的值添加到 roleMenuList 中
            menuMap.forEach((menuCode, roleMenu) -> {
                roleMenuList.add(roleMenu);
            });
            // 3.5 roleMenuList 加入到 roleMenuPermissionUserDTO中
            roleMenuPermissionUserDTO.setMenus(roleMenuList);
            // 4. 添加 roleMenuPermissionUserDTO 到集合
            roleMenuPermissionUserDTOS.add(roleMenuPermissionUserDTO);
        });

        return roleMenuPermissionUserDTOS;
    }
}
