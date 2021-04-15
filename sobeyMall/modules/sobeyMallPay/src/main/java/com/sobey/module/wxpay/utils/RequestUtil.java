package com.sobey.module.wxpay.utils;

import com.github.wxpay.sdk.WXPayUtil;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.wxpay.entity.orderQuery.OrderQuery;
import com.sobey.module.wxpay.entity.orderQuery.Result;
import com.sobey.module.wxpay.entity.unifiedorder.ResultBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 请求微信支付相关接口
 * @Author WuChenYang
 * @Since 2020/1/14 11:24
 */
public class RequestUtil {

    private static final Logger log = LoggerFactory.getLogger(RequestUtil.class);

    /**
     * 统一下单接口
     *
     * @param param 签完名的参数
     * @param url
     */
    public static ResultBody unifiedOrder(Map<String, String> param, String url) throws Exception {
        if (null == param || param.size() == 0 || StringUtils.isEmpty(url)) {
            return null;
        }
        String xml = WXPayUtil.mapToXml(param);
        log.info("请求微信统一下单接口参数:" + xml);
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(xml, "UTF-8");
        httpPost.setEntity(entity);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(httpPost);
        int code = response.getStatusLine().getStatusCode();
        HttpEntity resp = response.getEntity();
        String entityStr = EntityUtils.toString(resp, StandardCharsets.UTF_8);
        log.info("微信统一下单接口返回:code=" + code + ",response=" + entityStr);
        if (200 == code) {
            Map<String, String> map = WXPayUtil.xmlToMap(entityStr);
            //转换为实体类
            return EntityMapUtil.mapToEntity(map, ResultBody.class);
        }
        throw new AppException(ExceptionType.PAY_CODE_WX_PAY,code+"",new RuntimeException());
    }

    /**
     * 查询订单
     *
     * @param orderQuery
     * @param url
     */
    public static Result orderQuery(OrderQuery orderQuery, String url) throws Exception {

        if (null == orderQuery || StringUtils.isEmpty(url)) {
            return null;
        }
        Map<String, String> map = EntityMapUtil.entityToMap(orderQuery);
        String xml = WXPayUtil.mapToXml(map);
        log.info("请求微信查询订单接口参数:" + xml);
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(xml, StandardCharsets.UTF_8);
        httpPost.setEntity(entity);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(httpPost);
        int code = response.getStatusLine().getStatusCode();
        HttpEntity resp = response.getEntity();
        String entityStr = EntityUtils.toString(resp,StandardCharsets.UTF_8);
        log.info("微信查询订单接口返回:code=" + code + ",response=" + entityStr);
        if (200 == code) {
            Map<String, String> respMap = WXPayUtil.xmlToMap(entityStr);
            //转换为实体类
            Result result = EntityMapUtil.mapToEntity(respMap, Result.class);
            //判断是否有优惠券 第一张优惠券id下标从0开始
            int subNum = 0;
            Map<String, String> couponMap = new HashMap<>();
            while (true) {
                if (!respMap.containsKey("coupon_id_" + subNum)) {
                    break;
                }
                String id = respMap.get("coupon_id_" + subNum);
                String type = respMap.get("coupon_type_" + subNum);
                couponMap.put(id, type);
                subNum++;
            }
            if (couponMap.size() > 0) {
                result.setCoupon(couponMap);
            }
            return result;
        }
        throw new AppException(ExceptionType.PAY_CODE_WX_PAY,code+"",new RuntimeException());
    }

}
