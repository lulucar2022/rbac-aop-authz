package cn.lulucar.springbootshirovue.util;

import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.ObjectUtils;

/**
 * @author wenxiaolan
 * @ClassName PageFromRequestUtil
 * @date 2024/6/1 19:11
 * @description 从http请求中提取出分页参数
 */
public class PageFromRequestUtil {
    /**
     * 从http请求中获取分页参数
     * @param request http请求
     * @return 返回分页需要的参数
     */
    public static JSONObject getPage(HttpServletRequest request) {
        
        int current = Integer.parseInt(request.getParameter("current"));
        int size = Integer.parseInt(request.getParameter("size"));
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
