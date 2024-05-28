package cn.lulucar.springbootshirovue.service.impl;

import cn.lulucar.springbootshirovue.entity.Article;
import cn.lulucar.springbootshirovue.mapper.ArticleMapper;
import cn.lulucar.springbootshirovue.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author wenxiaolan
 * @since 2024-05-28
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
