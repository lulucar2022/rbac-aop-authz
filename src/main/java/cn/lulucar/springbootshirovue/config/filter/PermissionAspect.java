package cn.lulucar.springbootshirovue.config.filter;

import cn.lulucar.springbootshirovue.config.annotation.Logical;
import cn.lulucar.springbootshirovue.config.annotation.RequiresPermissions;
import cn.lulucar.springbootshirovue.config.exception.UnauthorizedException;
import cn.lulucar.springbootshirovue.dto.session.SessionUserInfo;
import cn.lulucar.springbootshirovue.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author wenxiaolan
 * @ClassName PermissionAspect
 * @date 2024/7/20 13:55
 * @description 角色权限控制
 */
@Aspect
@Slf4j
@Component
@Order(3)
public class PermissionAspect {
    private final TokenService tokenService;

    public PermissionAspect(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Before("@annotation(cn.lulucar.springbootshirovue.config.annotation.RequiresPermissions)")
    public void before(JoinPoint joinPoint) {
        log.debug("开始校验角色权限");
        SessionUserInfo userInfo = tokenService.getUserInfo();
        log.info("用户信息：{}",userInfo);
        List<String> userCodes = userInfo.getPermissionList();
        log.info("用户权限：{}",userCodes.toString());
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        RequiresPermissions annotation = methodSignature.getMethod().getAnnotation(RequiresPermissions.class);
        String[] value = annotation.value();
        log.debug("校验权限code：{}", Arrays.toString(value));
        log.debug("用户拥有的权限code：{}", userCodes);
        // 校验用户拥有的权限与操作的权限
        if (annotation.logical() == Logical.AND) {
            // 交集关系，需要包含每一个权限
            for (String perm : value) {
                if (!userCodes.contains(perm)) {
                    log.warn("用户权限不足，缺少权限：{}",perm);
                    throw new UnauthorizedException();
                }
            }
        } else {
            // 并集关系，只要包含一个权限即可
            boolean hasPermission = false;
            for (String perm : value) {
                if (userCodes.contains(perm)) {
                    hasPermission = true;
                    break;
                }
            }
            if (!hasPermission) {
                log.warn("用户权限不足，缺少任意一种权限：{}",Arrays.toString(value));
                throw new UnauthorizedException();
            }
        }

    }
        
}
