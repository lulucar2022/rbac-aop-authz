package cn.lulucar.springbootshirovue.util;

import org.springframework.util.ObjectUtils;

/**
 * @author wenxiaolan
 * @ClassName StringUtil
 * @date 2024/6/1 18:25
 * @description
 */
public class StringUtil {
    public static boolean isNullOrEmpty(String string) {
        return string.isEmpty() || string.equals("null");
    }
    
    public static boolean isNullOrEmpty(Object o) {
        return o.equals("") || ObjectUtils.isEmpty(o);
    }
}
