package cn.lulucar.springbootshirovue.util.constants;

import java.time.format.DateTimeFormatter;

/**
 * @author wenxiaolan
 * @ClassName Constants
 * @date 2024/5/29 21:10
 * @description 定义具体一个接口的成功消息和失败消息。
 */
public class Constants {
    public static final String SUCCESS_CODE = "200";    
    
    public static final String SUCCESS_MSG = "OK";

    // 时间格式
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
