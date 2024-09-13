package cn.lulucar.springbootshirovue.util;

import cn.lulucar.springbootshirovue.config.exception.CommonJsonException;
import cn.lulucar.springbootshirovue.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wenxiaolan
 * @ClassName RequiredParametersUtil
 * @date 2024/6/1 18:47
 * @description 验证是否有必填参数
 */
@Slf4j
public class RequiredParametersUtil {
    /**
     * 
     * @param jsonObject json数据
     * @param requiredColumns 必填参数（数组形式传入）
     */
    public static void hasAllRequired(final JSONObject jsonObject, String[] requiredColumns) {
        if (!StringUtil.isNullOrEmpty(requiredColumns)) {
            //验证字段非空
            List<String> missCols = new ArrayList<>();
            for (String requiredColumn : requiredColumns) {
                Object val = jsonObject.get(requiredColumn.trim());
                if (StringUtil.isNullOrEmpty(val)) {
                    missCols.add(requiredColumn);
                }
            }
            // 未填参数集合不为空，就抛出少了哪些参数。
            if (!missCols.isEmpty()) {
                log.info("缺少必填参数:" + missCols);
                jsonObject.clear();
                jsonObject.put("code", ErrorEnum.E_90003.getErrorCode());
                jsonObject.put("msg", "缺少必填参数:" + missCols.toString());
                jsonObject.put("data", new JSONObject());
                throw new CommonJsonException(jsonObject);
            }
        }
        
    }
}
