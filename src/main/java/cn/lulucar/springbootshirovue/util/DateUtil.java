package cn.lulucar.springbootshirovue.util;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author wenxiaolan
 * @ClassName isLegalDate
 * @date 2024/5/30 22:41
 * @description
 */
public class DateUtil {
    public static boolean isLegalDate(String dateTime, DateTimeFormatter formatter) {
        
        if (StringUtils.hasText(dateTime)) {
            return false;
        }
        
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
