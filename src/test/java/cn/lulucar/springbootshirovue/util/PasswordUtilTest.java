package cn.lulucar.springbootshirovue.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wenxiaolan
 * @ClassName PasswordUtilTest
 * @date 2024/6/3 19:59
 * @description
 */
class PasswordUtilTest {
    
    @Test
    void encryptPassword() {
        String s = PasswordUtil.encryptPassword("111111");
        boolean b = PasswordUtil.verifyPassword("111111", s);
        assertTrue(b);
    }
}