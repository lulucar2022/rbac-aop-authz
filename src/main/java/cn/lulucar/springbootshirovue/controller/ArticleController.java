package cn.lulucar.springbootshirovue.controller;

import cn.lulucar.springbootshirovue.config.exception.CommonJsonException;
import cn.lulucar.springbootshirovue.config.exception.ParameterFormatException;
import cn.lulucar.springbootshirovue.entity.Article;
import cn.lulucar.springbootshirovue.service.IArticleService;
import cn.lulucar.springbootshirovue.util.CommonUtil;
import cn.lulucar.springbootshirovue.util.PageFromRequestUtil;
import cn.lulucar.springbootshirovue.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author wenxiaolan
 * @ClassName ArticleController
 * @date 2024/5/28 15:43
 */
@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {
    private final IArticleService iArticleService;

    public ArticleController(IArticleService iArticleService) {
        this.iArticleService = iArticleService;
    }
    
    // 文章列表
    @GetMapping({"/",""})
    public JSONObject list(HttpServletRequest request) {
        JSONObject page = PageFromRequestUtil.getPage(request);
        Page<Article> result = iArticleService.listArticle(page);
        return CommonUtil.successJSON(result.getRecords());
    }
    
    // 新增文章
    @PostMapping({"/",""})
    public JSONObject add(@RequestBody Article article) {
        try {
            iArticleService.save(article);
            
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new CommonJsonException(ErrorEnum.E_90003);
        }

        return CommonUtil.successJSON();
    }
    
    // 修改文章
    @PutMapping({"/",""})   
    public JSONObject update(@RequestBody Article article) {
        try {
            iArticleService.updateById(article);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new CommonJsonException(ErrorEnum.E_90003);
        }
        return CommonUtil.successJSON();
    }
}
