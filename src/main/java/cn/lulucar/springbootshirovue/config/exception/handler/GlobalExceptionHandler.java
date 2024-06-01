package cn.lulucar.springbootshirovue.config.exception.handler;

import cn.lulucar.springbootshirovue.config.exception.CommonJsonException;
import cn.lulucar.springbootshirovue.config.exception.ParameterFormatException;
import cn.lulucar.springbootshirovue.util.CommonUtil;
import cn.lulucar.springbootshirovue.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author wenxiaolan
 * @ClassName GlobalExceptionHandler
 * @date 2024/5/30 22:14
 * @description
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(CommonJsonException.class)
    public JSONObject commonJsonExceptionHandler() {
        return CommonUtil.errorJSON(ErrorEnum.E_90003);
    } 
}
