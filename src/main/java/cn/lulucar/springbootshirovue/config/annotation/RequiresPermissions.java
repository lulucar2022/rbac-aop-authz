package cn.lulucar.springbootshirovue.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wenxiaolan
 * @ClassName RquiresPermissions
 * @date 2024/7/14 16:58
 * @description
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermissions {
    String[] value();
    
    Logical logical() default Logical.AND;
}
