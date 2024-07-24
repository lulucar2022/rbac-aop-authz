package cn.lulucar.springbootshirovue.config.exception;

import cn.lulucar.springbootshirovue.util.CommonUtil;
import cn.lulucar.springbootshirovue.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

/**
 * @author wenxiaolan
 * @ClassName CommonJsonException
 * @date 2024/6/1 18:54
 * @description 自定义错误类，用在校验参数
 */
@Getter
public class CommonJsonException extends RuntimeException{
    private final JSONObject resultJson;

    /**
     * 调用时可以在任何代码处直接throws这个Exception,
     * 都会统一被拦截,并封装好json返回给前台
     *
     * @param errorEnum 以错误的ErrorEnum做参数
     */
    public CommonJsonException(ErrorEnum errorEnum) {
        this.resultJson = CommonUtil.errorJSON(errorEnum);
    }

    public CommonJsonException(JSONObject resultJson) {
        this.resultJson = resultJson;
    }
    
}
