package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.entity.SysUser;
import cn.lulucar.springbootshirovue.mapper.SysUserMapper;
import cn.lulucar.springbootshirovue.service.ISysUserService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wenxiaolan
 * @ClassName SysUserServiceImplTest
 * @date 2024/6/3 12:05
 * @description
 */
@SpringBootTest
class SysUserServiceImplTest {

    @Autowired
    private ISysUserService  iSysUserService;
    @Disabled
    @Test
    void listUser() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("current",1);
        jsonObject.put("size",5);
        Page<SysUser> page = iSysUserService.listUser(jsonObject);
        System.out.println(page.getRecords());
        Assertions.assertNotNull(page);
    }
    
    @Disabled
    @Test
    void addUser() {
        SysUser user = new SysUser();
        user.setUsername("lulucar");
        user.setNickname("AAAAA");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setPassword("8888");
        iSysUserService.addUser(user);
    }
    
    @Test
    void countUser() {
        int count = iSysUserService.countUser();
        Assertions.assertEquals(5, count);
    }
}