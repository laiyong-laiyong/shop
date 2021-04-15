package com.sobey.module.common.util;

import com.alibaba.fastjson.JSON;
import com.sobey.framework.httpclient.RestCaller;
import com.sobey.module.fegin.order.entity.request.ProductParam;
import com.sobey.module.fegin.order.entity.response.Result;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/17 13:45
 */
public class ReqUtil {

    /**
     * 开通或续费商品
     * @param param
     * @return
     */
    public static Result operaProd(ProductParam param,String url){
        return RestCaller.post(url).setBody(JSON.toJSONString(param)).executeReturn(Result.class);
    }

}
