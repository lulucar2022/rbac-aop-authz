package cn.lulucar.springbootshirovue.controller;

import cn.lulucar.springbootshirovue.config.annotation.Logical;
import cn.lulucar.springbootshirovue.config.annotation.RequiresPermissions;
import cn.lulucar.springbootshirovue.dto.RoleDTO;
import cn.lulucar.springbootshirovue.dto.RolePermissionDTO;
import cn.lulucar.springbootshirovue.dto.UserAndRoleDTO;
import cn.lulucar.springbootshirovue.dto.UserForUpdateDTO;
import cn.lulucar.springbootshirovue.entity.SysRole;
import cn.lulucar.springbootshirovue.entity.SysUser;
import cn.lulucar.springbootshirovue.service.ISysPermissionService;
import cn.lulucar.springbootshirovue.service.ISysRoleService;
import cn.lulucar.springbootshirovue.service.ISysUserService;
import cn.lulucar.springbootshirovue.util.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
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
    public UserController(ISysRoleService iSysRoleService, ISysUserService iSysUserService, ISysPermissionService iSysPermissionService) {
        this.iSysRoleService = iSysRoleService;
        this.iSysUserService = iSysUserService;
        this.iSysPermissionService = iSysPermissionService;
    }

    // 查询用户列表
    @RequiresPermissions("user:list")
    @GetMapping({"/",""}) 
    public JSONObject list(@RequestParam("current") Long current,
                           @RequestParam("size") Long size){
        Page<SysUser> page = iSysUserService.listUser(current, size);
        return CommonUtil.successJSON(page);
        
    }
    // 新增用户
    @RequiresPermissions("user:add")
    @PostMapping("/")
    public JSONObject add(@RequestBody UserAndRoleDTO param) {
        SysUser user = new SysUser();
        user.setUsername(param.getUsername());
        user.setPassword(param.getPassword());
        user.setNickname(param.getNickname());

        List<Integer> roles = param.getRoleId();
        iSysUserService.addUser(user,roles);
        return CommonUtil.successJSON();
    }
    // 更新用户 （参数：nickname，roleIds，deleteStatus，userId）需要新增一个DTO类
    @RequiresPermissions("user:update")
    @PatchMapping("/")
    public JSONObject update(@RequestBody UserForUpdateDTO userForUpdateDTO) {
        SysUser user = new SysUser();
        user.setId(userForUpdateDTO.getUserId());
        
        user.setNickname(userForUpdateDTO.getNickname());
        user.setDeleteStatus(userForUpdateDTO.getDeleteStatus());
        Set<Integer> roleIds = userForUpdateDTO.getRoleIds();
        iSysUserService.updateUser(user,roleIds);
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
        return iSysRoleService.listAllRoles();
    }
    // 查询所有权限，给角色分配权限
    @RequiresPermissions("role:list")
    @GetMapping("/listAllPermissions")
    public JSONObject listAllPermissions(){
        return iSysPermissionService.listAllPermission();
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
