package cn.lulucar.springbootshirovue;

import cn.lulucar.springbootshirovue.entity.SysUser;
import cn.lulucar.springbootshirovue.mapper.ArticleMapper;
import cn.lulucar.springbootshirovue.entity.Article;
import cn.lulucar.springbootshirovue.mapper.SysUserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringbootShiroVueApplicationTests {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Test
    void contextLoads() {
        List<SysUser> list = sysUserMapper.selectList(null);
        Assertions.assertEquals(5, list.size());
        list.forEach(System.out::println);
    }

}
