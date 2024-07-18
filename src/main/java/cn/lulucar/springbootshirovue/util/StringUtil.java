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
        return null == string || string.isEmpty() || "null".equals(string);
    }
    
    public static boolean isNullOrEmpty(Object o) {
        return null == o || "".equals(o);
    }
}
