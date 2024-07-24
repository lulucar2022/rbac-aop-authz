package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.entity.SysRole;
import cn.lulucar.springbootshirovue.mapper.SysRoleMapper;
import cn.lulucar.springbootshirovue.service.ISysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
        role.setId(7);
        role.setRoleName("半夜管理员");
        Set<Integer> perms = new HashSet<>();
        perms.add(101);
        perms.add(102);
        perms.add(103);
        iSysRoleService.updateRole(role, perms);
    }

    @Test
    void deleteRole() {
    }

    
    @DisplayName("修改角色名称")
    @ParameterizedTest
    @ValueSource(strings = {"大大大管理员","小小小管理员"})
    void updateRoleName(String name) {
        SysRole role = new SysRole();
        role.setId(7);
        role.setRoleName(name);
        boolean b = iSysRoleService.updateRoleName(role);
        Assertions.assertTrue(b);
    }

    
}