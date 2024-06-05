package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.service.ISysRolePermissionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

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
}