package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.entity.SysUser;
import cn.lulucar.springbootshirovue.mapper.SysUserMapper;
import cn.lulucar.springbootshirovue.service.ISysUserService;
import cn.lulucar.springbootshirovue.util.PasswordUtil;
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
    
    
    @Test
    void addUser() {
        SysUser user = new SysUser();
        user.setUsername("lulucar22222");
        user.setNickname("AAAAA");
        String password = PasswordUtil.encryptPassword("8888");
        user.setPassword(password);
        iSysUserService.addUser(user);
    }
    
    @Disabled
    @Test
    void countUser() {
        int count = iSysUserService.countUser();
        Assertions.assertEquals(5, count);
    }
}