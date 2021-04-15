package com.sobey.module.utils;

import com.sobey.module.order.entity.ResultInfo;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * @author WCY
 * @createTime 2020/5/20 13:50
 * 校验参数是否为空
 */
public class ValidateUtil {

    /**
     * 校验必传参数是否为空,适用于参数注解存在{@link io.swagger.annotations.ApiModelProperty}的情况
     * @param bean
     * @return
     */
    public static ResultInfo validateBeanParam(Object bean) throws IllegalAccessException{

        if (null == bean){
            return ResultInfo.withFail("参数为空");
        }
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            if (null == annotation){
                continue;
            }
            if (annotation.required()){
                Object val = field.get(bean);
                if (null == val || StringUtils.isBlank(val.toString())){
                    return ResultInfo.withFail(field.getName()+"为空");
                }
            }
        }
        return ResultInfo.withSuccess("");
    }

}
