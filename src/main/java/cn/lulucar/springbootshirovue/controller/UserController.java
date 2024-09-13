package cn.lulucar.springbootshirovue.controller;

import cn.lulucar.springbootshirovue.config.annotation.Logical;
import cn.lulucar.springbootshirovue.config.annotation.RequiresPermissions;
import cn.lulucar.springbootshirovue.dto.*;
import cn.lulucar.springbootshirovue.entity.SysRole;
import cn.lulucar.springbootshirovue.entity.SysUser;
import cn.lulucar.springbootshirovue.service.ISysPermissionService;
import cn.lulucar.springbootshirovue.service.ISysRoleService;
import cn.lulucar.springbootshirovue.service.ISysUserService;
import cn.lulucar.springbootshirovue.service.impl.CoordinatorService;
import cn.lulucar.springbootshirovue.util.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author wenxiaolan
 * @ClassName UserController
 * @date 2024/5/31 17:51
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private final ISysRoleService iSysRoleService;
    private final ISysUserService iSysUserService;
    private final ISysPermissionService iSysPermissionService;
    private final CoordinatorService coordinatorService;
    
    public UserController(ISysRoleService iSysRoleService, ISysUserService iSysUserService, ISysPermissionService iSysPermissionService, CoordinatorService coordinatorService) {
        this.iSysRoleService = iSysRoleService;
        this.iSysUserService = iSysUserService;
        this.iSysPermissionService = iSysPermissionService;
        this.coordinatorService = coordinatorService;
    }

    // 查询用户列表
    @RequiresPermissions("user:list")
    @GetMapping({"/",""}) 
    public JSONObject list(@RequestParam("pageNum") Long pageNum,
                           @RequestParam("pageRow") Long pageRow){
        List<UserAndRoleDTO> list = iSysUserService.listUserAndRole(pageNum, pageRow);
        return CommonUtil.successJSON(list);
        
    }
    // 新增用户
    @RequiresPermissions("user:add")
    @PostMapping({"/",""})
    public JSONObject add(@RequestBody UserForAddDTO param) {
        SysUser user = new SysUser();
        user.setUsername(param.getUsername());
        user.setPassword(param.getPassword());
        user.setNickname(param.getNickname());

        List<Integer> roleIds = param.getRoleIds();
        iSysUserService.addUser(user,roleIds);
        return CommonUtil.successJSON();
    }
    // 更新用户 （参数：nickname，roleIds，deleteStatus，userId）需要新增一个DTO类
    @RequiresPermissions("user:update")
    @PatchMapping({"/",""})
    public JSONObject update(@RequestBody UserForUpdateDTO userForUpdateDTO) {
        SysUser user = new SysUser();
        user.setId(userForUpdateDTO.getUserId());
        user.setNickname(userForUpdateDTO.getNickname());
        user.setDeleteStatus(userForUpdateDTO.getDeleteStatus());
        Set<Integer> roleIds = userForUpdateDTO.getRoleIds();
        iSysUserService.updateUser(user,roleIds);
        return CommonUtil.successJSON();
    }
    // 删除用户 （参数：userId）
    @RequiresPermissions("user:update")
    @DeleteMapping({"/",""})
    public JSONObject delete(@RequestBody UserForUpdateDTO params) {
        Integer userId = params.getUserId();
        Set<Integer> roleIds = params.getRoleIds();
        iSysUserService.deleteUser(userId,roleIds);
        return CommonUtil.successJSON();
    }
    // 查询所有角色（参数：roleId，roleName）
    @RequiresPermissions(value={"user:add","user:update"}, logical = Logical.OR)
    @GetMapping("/role/getAllRoles")
    public JSONObject getAllRoles() {

        List<RoleDTO> allRoles = iSysRoleService.getAllRoles();
        return CommonUtil.successJSON(allRoles);
    }
    // 角色列表
    @RequiresPermissions("role:list")
    @GetMapping("/listAllRoles")
    public JSONObject listAllRoles(){
        List<RoleMenuPermissionUserDTO> roleMenuPermissionUserDTOS = coordinatorService.listAllRoles();
        JSONObject list = new JSONObject();
        list.put("list",roleMenuPermissionUserDTOS);
        return CommonUtil.successJSON(list);
    }
    
    // 查询所有权限，给角色分配权限
    @RequiresPermissions("role:list")
    @GetMapping("/listAllPermissions")
    public JSONObject listAllPermissions(){
        JSONObject listAllPermission = iSysPermissionService.listAllPermission();
        return CommonUtil.successJSON(listAllPermission);
    }
    
    // 新增角色 （参数：roleName， permissions集合）
    @RequiresPermissions("role:add")
    @PostMapping("/role")
    public JSONObject addRole(@RequestBody RolePermissionDTO rolePermissionDTO){
        SysRole role = new SysRole();
        role.setRoleName(rolePermissionDTO.getRoleName());
        Set<Integer> permissions = rolePermissionDTO.getPermissions();
        iSysRoleService.addRole(role,permissions);
        return CommonUtil.successJSON();
    }
    
    // 修改角色 （参数：roleId，roleName，permissions集合）
    @RequiresPermissions("role:update")
    @PatchMapping("/role")
    public JSONObject updateRole(@RequestBody RolePermissionDTO rolePermissionDTO) {
        SysRole role = new SysRole();
        role.setId(rolePermissionDTO.getRoleId());
        role.setRoleName(rolePermissionDTO.getRoleName());
        Set<Integer> permissions = rolePermissionDTO.getPermissions();
        iSysRoleService.updateRole(role,permissions);
        return CommonUtil.successJSON();
    }
    
    // 删除角色
    @RequiresPermissions("role:delete")
    @DeleteMapping("/role/{id}")
    public JSONObject deleteRole(@PathVariable Integer id) {
        SysRole role = new SysRole();
        role.setId(id);
        iSysRoleService.deleteRole(role);
        return CommonUtil.successJSON();
    }
}
