package com.sobey.module.utils;

import com.alibaba.fastjson.JSON;
import com.sobey.framework.httpclient.RestCaller;
import com.sobey.module.order.entity.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/17 13:45
 */
public class ReqUtil {

    /**
     * 开通或续费或关闭商品
     *
     * @param param
     * @return
     */
    public static Result operaProd(String token, Object param, String url) {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        return RestCaller.post(url).addHeader(header).setBody(JSON.toJSONString(param)).executeReturn(Result.class);
    }

}
