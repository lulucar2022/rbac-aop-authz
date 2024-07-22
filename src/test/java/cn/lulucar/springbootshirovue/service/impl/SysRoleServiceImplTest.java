package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.entity.SysRole;
import cn.lulucar.springbootshirovue.mapper.SysRoleMapper;
import cn.lulucar.springbootshirovue.service.ISysRoleService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wenxiaolan
 * @ClassName SysRoleServiceImplTest
 * @date 2024/6/5 16:24
 * @description
 */
@SpringBootTest
class SysRoleServiceImplTest {

    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Test
    void listRole() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("current",1L);
        jsonObject.put("size",3L);

        Page<SysRole> page = iSysRoleService.listRole(jsonObject);
        System.out.println(page.getRecords());
        System.out.println(page.getPages());
        Assertions.assertEquals(3,page.getSize());
    }

    @Disabled
    @Test
    void addRole() {
        SysRole role = new SysRole();
        role.setRoleName("测试员");
        Set<Integer> permissions = new HashSet<>();
        permissions.add(101);
        permissions.add(102);
        permissions.add(103);
        iSysRoleService.addRole(role,permissions);
    }

    @Disabled
    @Test
    void updateRole() {
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRole::getId,1);
        SysRole role = sysRoleMapper.selectOne(lambdaQueryWrapper);
        role.setRoleName("超级管理员");
        Set<Integer> perms = new HashSet<>();
        perms.add(101);
        perms.add(102);
        perms.add(103);
        boolean b = iSysRoleService.updateRole(role, perms);
        Assertions.assertTrue(b);
    }

    @Test
    void deleteRole() {
    }

    @Disabled
    @Test
    void updateRoleName() {
        SysRole role = new SysRole();
        role.setRoleName("管理员2");
        boolean b = iSysRoleService.updateRoleName(role);
        Assertions.assertTrue(b);
    }
}