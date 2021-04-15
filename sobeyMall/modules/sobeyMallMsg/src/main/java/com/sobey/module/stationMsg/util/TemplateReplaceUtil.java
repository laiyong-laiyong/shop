package com.sobey.module.stationMsg.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @Author WCY
 * @CreateTime 2020/4/13 11:00
 * 模板处理类
 */
public class TemplateReplaceUtil {

    /**
     * 替换模板占位符
     * @param content
     * @param replacements
     * @return
     */
    public static String replace(String content, Map<String,String> replacements){
        if (StringUtils.isBlank(content)){
            return null;
        }
        if (null != replacements && replacements.size() > 0){
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                content = StringUtils.replace(content,entry.getKey(),entry.getValue());
            }
        }
        return content;
    }
    
}
