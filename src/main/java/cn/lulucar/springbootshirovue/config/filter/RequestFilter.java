package cn.lulucar.springbootshirovue.config.filter;

import cn.lulucar.springbootshirovue.config.exception.CommonJsonException;
import cn.lulucar.springbootshirovue.dto.session.SessionUserInfo;
import cn.lulucar.springbootshirovue.service.TokenService;
import cn.lulucar.springbootshirovue.service.impl.TokenServiceImpl;
import cn.lulucar.springbootshirovue.util.StringUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.UUID;

/**
 * @author wenxiaolan
 * @ClassName RequestFilter
 * @date 2024/7/18 16:21
 * @description
 */

@Component
@Slf4j
public class RequestFilter extends OncePerRequestFilter implements Filter {
    private final TokenService tokenService;

    public RequestFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * @param request 
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 每个请求记录一个 traceId，可以根据 traceId 搜索出本次请求的全部相关日志
            MDC.put("traceId", UUID.randomUUID().toString().replace("-", "").substring(0, 12));
            // todo
            setUsername(request);
            setProductId(request);
            
            request = new ContentCachingRequestWrapper(request);
            filterChain.doFilter(request,response);
        } catch (Exception e) {
            throw e;
        } finally {
            MDC.clear();
        }
    }

    private void setProductId(HttpServletRequest request) {
        String productId = request.getParameter("productId");
        if (!StringUtil.isNullOrEmpty(productId)) {
            log.debug("url 的 productId:{}", productId);
            MDC.put("productId", productId);
        }
    }

    private void setUsername(HttpServletRequest request) {
        // 通过 token 解析出 username
        String token = request.getHeader("token");
        if (!StringUtil.isNullOrEmpty(token)) {
            MDC.put("token", token);
            try {
                SessionUserInfo info = tokenService.getUserInfo();
                if (info != null) {
                    String username = info.getUsername();
                    MDC.put("username",username);
                }
            } catch (CommonJsonException e) {
                log.info("无效 token:{}",token);
            }
        }
    }
}
