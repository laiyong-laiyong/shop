package com.sobey.module.common.util;

import com.sobey.module.common.response.ResultInfo;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * @author WCY
 * @createTime 2020/7/20 16:59
 * 校验参数工具类
 */
public class ValidateUtil {

    public static ResultInfo validate(Field[] fields, Object obj) throws IllegalAccessException {
        ResultInfo rt = new ResultInfo();
        for (Field field : fields) {
            field.setAccessible(true);
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            if (annotation == null) {
                continue;
            }
            if (annotation.required()) {
                Object val = field.get(obj);
                if (null == val || StringUtils.isBlank(val.toString())) {
                    rt.setRt_code("FAIL");
                    rt.setRt_msg("参数" + field.getName() + "为空");
                    return rt;
                }
            }
        }
        rt.setRt_code("SUCCESS");
        rt.setRt_msg("");
        return rt;
    }

}
