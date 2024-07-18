package cn.lulucar.springbootshirovue.config.system;

import cn.lulucar.springbootshirovue.dto.session.SessionUserInfo;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * @author wenxiaolan
 * @ClassName CacheConfig
 * @date 2024/7/18 1:38
 * @description
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 配置默认的缓存管理器
     */
    @Primary
    @Bean("defaultCacheManager")
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 设置最后一次写入后经过固定时间过期.
                .expireAfterWrite(10, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000));
        return cacheManager;
    }

    /**
     * token 放在本地缓存
     * @return
     */
    @Bean("tokenCacheManager")
    public Cache<String, SessionUserInfo> caffeineCache() {
        return Caffeine.newBuilder()
                // 设置最近一次访问后刷新过期时间
                .expireAfterAccess(30L, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(10000)
                .build();
    }
}
