package cn.lulucar.springbootshirovue.util;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.ObjectUtils;

/**
 * @author wenxiaolan
 * @ClassName PageFromRequestUtil
 * @date 2024/6/1 19:11
 * @description 从http请求中提取出分页参数
 */
public class PageFromRequestUtil {
    
    public static JSONObject getPage(HttpServletRequest request) {
        Integer current = Integer.valueOf(request.getParameter("current"));
        Integer size = Integer.valueOf(request.getParameter("size"));
        if (ObjectUtils.isEmpty(current) || ObjectUtils.isEmpty(size)) {
            current =1;
            size = 8;
        }
        JSONObject page = new JSONObject();
        page.put("current",current);
        page.put("size",size);
        return page;
    }
}
