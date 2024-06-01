package cn.lulucar.springbootshirovue.util;

import cn.lulucar.springbootshirovue.util.constants.Constants;
import cn.lulucar.springbootshirovue.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;

/**
 * @author wenxiaolan
 * @ClassName CommonUtil
 * @date 2024/5/29 21:02
 * @description 处理数据json工具类
 */
public class CommonUtil {

    /**
     * 返回data为空的成功消息json
     * @return 成功响应结果
     */
    public static JSONObject successJSON() {
        return successJSON(new JSONObject());
    }

    /**
     * 响应结果三个部分：code、msg、data
     * 返回状态码200的成功信息json
     * @param data 成功消息
     * @return 成功响应结果
     */
    public static JSONObject successJSON(Object data) {
        JSONObject resultJSON = new JSONObject();
        resultJSON.put("code", Constants.SUCCESS_CODE);
        resultJSON.put("msg",Constants.SUCCESS_MSG);
        resultJSON.put("data",data);
        return resultJSON;
    }

    /**
     * 返回错误信息json
     * @param errorEnum 错误信息枚举对象
     * @return 失败响应结果
     */
    public static JSONObject errorJSON(ErrorEnum errorEnum) {
        JSONObject resultJSON = new JSONObject();
        resultJSON.put("code",errorEnum.getErrorCode());
        resultJSON.put("msg", errorEnum.getErrorMsg());
        // 返回空数据
        resultJSON.put("data", new JSONObject());
        return resultJSON;
    }
}
