package com.sobey.module.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sobey.framework.spring.SpringContextHolder;
import com.sobey.module.common.request.NoticePackParam;
import com.sobey.module.common.request.NoticePackResource;
import com.sobey.module.fegin.mallPack.packOrder.entity.request.MallPackResource;
import com.sobey.module.fegin.order.entity.response.Result;
import com.sobey.module.fegin.product.request.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author WCY
 * @createTime 2020/8/13 10:51
 * 购买套餐包时通知对应商家
 */
@Component
public class NoticePackUtil {

    private static final Logger log = LoggerFactory.getLogger(NoticePackUtil.class);

    @Autowired
    private ProductService ps;

    /**
     * 用户购买成功通知卖家
     *
     * @param productId
     * @param userCode
     * @param siteCode
     * @param expireDate
     * @param mallPackResources
     * @return
     */
    @Async("executors")
    public void noticePack(String token,
                           String packUuid,
                           String packName,
                           String productId,
                           String userCode,
                           String siteCode,
                           Date expireDate,
                           List<MallPackResource> mallPackResources) {
        try {
            JSONArray array = ps.queryProduct(token, productId);
            JSONObject product = array.getJSONObject(0);
            String enablePackagesNotify = product.getString("enablePackagesNotify");
            //未开启套餐包通知
            if (!"1".equals(enablePackagesNotify)) {
                return;
            }
            //创建套餐包购买通知参数
            NoticePackParam param = new NoticePackParam();
            List<NoticePackResource> resources = new ArrayList<>();
            param.setPackUuid(packUuid);
            param.setPackName(packName);
            param.setUserCode(userCode);
            param.setSiteCode(siteCode);
            param.setExpireDate(DateUtil.format(expireDate, DateUtil.FORMAT_1));
            for (MallPackResource mallPackResource : mallPackResources) {
                NoticePackResource resource = new NoticePackResource();
                BeanUtils.copyProperties(mallPackResource, resource);
                resources.add(resource);
            }
            param.setResources(resources);
            String notifyPackagesUrl = product.getString("notifyPackagesUrl");
            RestTemplate restTemplate = SpringContextHolder.getBean(RestTemplate.class);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token);
            HttpEntity<NoticePackParam> httpEntity = new HttpEntity<>(param, headers);
            log.info("调用外部购买套餐包通知接口,请求头:{},参数:{}", JSON.toJSONString(headers, SerializerFeature.WriteNullStringAsEmpty), JSON.toJSONString(param, SerializerFeature.WriteNullStringAsEmpty));
            ResponseEntity<Result> rs = restTemplate.exchange(notifyPackagesUrl, HttpMethod.POST, httpEntity, Result.class);
            Result body = rs.getBody();
            log.info("调用外部购买套餐包通知接口返回:{}", JSON.toJSONString(body, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue));
        } catch (Exception e) {
            log.info("调用外部购买套餐包通知接口发生异常:",e);
        }
    }

}
