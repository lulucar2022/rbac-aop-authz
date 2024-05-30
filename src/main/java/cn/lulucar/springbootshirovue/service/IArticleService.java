package cn.lulucar.springbootshirovue.service;

import cn.lulucar.springbootshirovue.entity.Article;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
public interface IArticleService extends IService<Article> {
    // 新增文章
    boolean save(Article article);
    // 查询文章
    Page<Article> listArticle(JSONObject article);
    // 修改文章
    boolean updateById(Article article);
}
