package cn.lulucar.springbootshirovue;

import cn.lulucar.springbootshirovue.entity.SysUser;
import cn.lulucar.springbootshirovue.mapper.ArticleMapper;
import cn.lulucar.springbootshirovue.entity.Article;
import cn.lulucar.springbootshirovue.mapper.SysUserMapper;
import cn.lulucar.springbootshirovue.service.IArticleService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class SpringbootShiroVueApplicationTests {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private IArticleService iArticleService;
    
    @Test
    void contextLoads() {
        JSONObject article = new JSONObject();
        article.put("current",1);
        article.put("size",10);
        Page<Article> page = iArticleService.listArticle(article);
        
        System.out.println(page.getRecords());
        
        Assertions.assertNotNull(page);
    }
    @Test
    void ArticleAdd() {
        Article article = new Article();
        article.setContent("12312312313123123123123");
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        
        iArticleService.save(article);
    }
    
    @Test
    void ArticleUpdate() {
        Article article = new Article();
        article.setId(22);
        article.setContent("kankandwadawkankan");
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        
        iArticleService.updateById(article);
    }

}
