package cn.lulucar.springbootshirovue.controller;

import cn.lulucar.springbootshirovue.service.IArticleService;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wenxiaolan
 * @ClassName ArticleController
 * @date 2024/5/28 15:43
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    private final IArticleService iArticleService;

    public ArticleController(IArticleService iArticleService) {
        this.iArticleService = iArticleService;
    }
    
    // @GetMapping("/list")
    // public JSONObject list(HttpServletRequest request) {
    //    
    // }
}
