package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.entity.SysRole;
import cn.lulucar.springbootshirovue.service.ISysRoleService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Test
    void addRole() {
        
    }

    @Test
    void updateRole() {
    }

    @Test
    void deleteRole() {
    }
}