package cn.lulucar.springbootshirovue.config.exception.handler;

import cn.lulucar.springbootshirovue.config.exception.CommonJsonException;
import cn.lulucar.springbootshirovue.config.exception.ParameterFormatException;
import cn.lulucar.springbootshirovue.util.CommonUtil;
import cn.lulucar.springbootshirovue.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author wenxiaolan
 * @ClassName GlobalExceptionHandler
 * @date 2024/5/30 22:14
 * @description
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public JSONObject defaultErrorHandler(HttpServletRequest request, Exception e) {
        String errorPosition = "";
        // 错误栈信息中找到发生错误的位置（文件名：行数）
        if (e.getStackTrace().length>0) {
            StackTraceElement element = e.getStackTrace()[0];
            String fileName = element.getFileName() == null ? "未找到错误文件" : element.getFileName();
            int lineNumber = element.getLineNumber();
            errorPosition = fileName + ":" + lineNumber;
        }
        JSONObject jsonObject = new JSONObject();
        // 错误代码
        jsonObject.put("code",ErrorEnum.E_400.getErrorCode());
        // 错误信息
        jsonObject.put("msg",ErrorEnum.E_400.getErrorMsg());
        // 错误发生位置
        JSONObject errorJson = new JSONObject();
        errorJson.put("errorLocation",e + "，错误位置：" + errorPosition);
        // 存入 返回结果中
        jsonObject.put("info",errorJson);
        log.error("异常",e);
        return jsonObject;
    }

    /**
     * GET/POST请求方法错误的拦截器
     * 因为开发时可能比较常见,而且发生在进入controller之前,上面的拦截器拦截不到这个错误
     * 所以定义了这个拦截器
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public JSONObject httpRequestMethodHandler() {
        return CommonUtil.errorJSON(ErrorEnum.E_500);
    }

    /**
     * 本系统自定义错误的拦截器
     * 拦截到此错误之后,就返回这个类里面的json给前端
     * 常见使用场景是参数校验失败,抛出此错,返回错误信息给前端
     */
    @ExceptionHandler(CommonJsonException.class)
    public JSONObject commonJsonExceptionHandler(CommonJsonException commonJsonException) {
        return commonJsonException.getResultJson();
    }

    /**
     * 权限不足报错拦截
     */
    @ExceptionHandler(UnauthorizedException.class)
    public JSONObject unauthorizedExceptionHandler() {
        return CommonUtil.errorJSON(ErrorEnum.E_502);
    }

    /**
     * 未登录报错拦截
     * 在请求需要权限的接口,而连登录都还没登录的时候,会报此错
     */
    @ExceptionHandler(UnauthenticatedException.class)
    public JSONObject unauthenticatedException() {
        return CommonUtil.errorJSON(ErrorEnum.E_20011);
    }
}
