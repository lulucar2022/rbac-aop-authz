package cn.lulucar.springbootshirovue.controller;

import cn.lulucar.springbootshirovue.config.annotation.RequiresPermissions;
import cn.lulucar.springbootshirovue.dto.UserAndRoleDTO;
import cn.lulucar.springbootshirovue.entity.SysUser;
import cn.lulucar.springbootshirovue.service.ISysPermissionService;
import cn.lulucar.springbootshirovue.service.ISysRoleService;
import cn.lulucar.springbootshirovue.service.ISysUserService;
import cn.lulucar.springbootshirovue.util.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    //todo 更新用户 （参数：nickname，roleIds，deleteStatus，userId）需要新增一个DTO类
    @RequiresPermissions("user:update")
    @PutMapping("/")
    public JSONObject update() {
        return null;
    }
    //todo 查询所有角色（参数：roleId，roleName）需要新增一个DTO类
    @RequiresPermissions(value={"user:add","user:update"})
    @GetMapping("/getAllRoles")
    public JSONObject getAllRoles() {
        return null;
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
    // todo 新增角色
    @RequiresPermissions("role:add")
    @PostMapping("/role")
    public JSONObject addRole(){
        return null;
    }
    // todo 修改角色
    @RequiresPermissions("role:update")
    @PutMapping("/role")
    public JSONObject updateRole() {
        return null;
    }
    // todo 删除角色
    @RequiresPermissions("role:delete")
    @PutMapping("/role")
    public JSONObject deleteRole() {
        
        return null;
    }
}
