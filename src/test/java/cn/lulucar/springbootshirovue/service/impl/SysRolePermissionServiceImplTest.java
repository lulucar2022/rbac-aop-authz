package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.entity.SysRolePermission;
import cn.lulucar.springbootshirovue.service.ISysRolePermissionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wenxiaolan
 * @ClassName SysRolePermissionServiceImplTest
 * @date 2024/6/5 23:10
 * @description
 */
@SpringBootTest
class SysRolePermissionServiceImplTest {

    @Autowired
    private ISysRolePermissionService iSysRolePermissionService;
    @Test
    void insertRolePermission() {
        List<Integer> permissionIds = new ArrayList<>();
        permissionIds.add(101);
        permissionIds.add(102);
        permissionIds.add(103);
        boolean b = iSysRolePermissionService.insertRolePermission(1, permissionIds);
        Assertions.assertTrue(b,"插入成功");
    }

    @Test
    void getRoleAllPermissions() {
        int roleId = 1;
        List<SysRolePermission> roleAllPermissions = iSysRolePermissionService.getRoleAllPermissions(roleId);
        System.out.println(roleAllPermissions.toString());
        Assertions.assertEquals(3,roleAllPermissions.size());
        
    }
    
    @Test
    void removeRolePermission() {
        Set<Integer> permissions = new HashSet<>();
        permissions.add(101);
        permissions.add(102);
        permissions.add(103);
        permissions.add(103);
        boolean b = iSysRolePermissionService.removeRolePermission(1, permissions);
        Assertions.assertTrue(b);
    }
}