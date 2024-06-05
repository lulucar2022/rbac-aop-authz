package cn.lulucar.springbootshirovue.config.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author wenxiaolan
 * @ClassName MybatisPlusMetaObjectHandler
 * @date 2024/6/5 22:42
 * @description
 */
@Component
@Slf4j
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {
    /**
     * 自动填充 create_time 字段
     * @param metaObject 
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始插入填充");
        this.strictInsertFill(metaObject,"createTime", LocalDateTime.class,LocalDateTime.now());
        this.strictInsertFill(metaObject,"updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    /**
     * 自动填充 update_time 字段
     * @param metaObject 
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始更新填充");
        this.strictUpdateFill(metaObject,"updateTime", LocalDateTime.class,LocalDateTime.now());
    }
}
