package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.config.exception.CommonJsonException;
import cn.lulucar.springbootshirovue.config.exception.ParameterFormatException;
import cn.lulucar.springbootshirovue.entity.Article;
import cn.lulucar.springbootshirovue.mapper.ArticleMapper;
import cn.lulucar.springbootshirovue.service.IArticleService;
import cn.lulucar.springbootshirovue.util.DateUtil;
import cn.lulucar.springbootshirovue.util.constants.Constants;
import cn.lulucar.springbootshirovue.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
@Slf4j
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    private final ArticleMapper articleMapper;

    public ArticleServiceImpl(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    /**
     * 新增文章
     * @param article 文章实体
     * @return
     */
    @Override
    public boolean save(Article article) {
        // 参数判断
        if (article.getContent().isBlank()) {
            throw new CommonJsonException(ErrorEnum.E_90003);
        }
        // 创建时间和更新时间
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.insert(article);
        return true;
    }

   

    /**
     * 查询文章
     * @param article 分页参数
     * @return 返回文章列表
     */
    @Override
    public Page<Article> listArticle(JSONObject article) {
        Page<Article> page = new Page<>(article.getInteger("current"), article.getInteger("size"));
        return articleMapper.selectPage(page,null);
    }

    /**
     * 更新文章
     * @param article 文章实体
     * @return
     */
    @Override
    public boolean updateById(Article article) {
        // 参数判断
        if (article.getContent().isBlank()) {
            throw new CommonJsonException(ErrorEnum.E_90003);
        }
        
        articleMapper.updateById(article);
        
        return true;
    }

    
}
