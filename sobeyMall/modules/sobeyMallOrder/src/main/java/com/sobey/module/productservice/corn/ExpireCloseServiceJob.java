package com.sobey.module.productservice.corn;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sobey.framework.spring.SpringContextHolder;
import com.sobey.module.fegin.msg.enumeration.MsgSubType;
import com.sobey.module.fegin.product.request.service.ProductService;
import com.sobey.module.order.ServiceStatus;
import com.sobey.module.order.common.OpenType;
import com.sobey.module.order.entity.Result;
import com.sobey.module.productservice.destroy.DestroyServicePublisher;
import com.sobey.module.productservice.entity.charge.Charge;
import com.sobey.module.productservice.entity.charge.CloseParam;
import com.sobey.module.productservice.model.ServiceInfo;
import com.sobey.module.productservice.service.ServiceInfoService;
import com.sobey.module.utils.CacheUtil;
import com.sobey.module.utils.MsgUtil;
import com.sobey.module.utils.ReqUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author WCY
 * @createTime 2020/5/6 9:50
 * 过期关闭服务定时
 */
public class ExpireCloseServiceJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(ExpireCloseServiceJob.class);
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("扫描服务是否过期任务执行");
        ServiceInfoService sis = SpringContextHolder.getBean(ServiceInfoService.class);
        DestroyServicePublisher destroyServicePublisher = SpringContextHolder.getBean(DestroyServicePublisher.class);
        Date date = new Date();
        List<ServiceInfo> list = sis.findExpiredService(date, ServiceStatus.Closed.getCode());
        if (null != list && list.size() > 0) {
            List<ServiceInfo> updates = new ArrayList<>();
            for (ServiceInfo serviceInfo : list) {
                if (ServiceStatus.Closed.getCode().equalsIgnoreCase(serviceInfo.getServiceStatus())) {
                    continue;
                }
                ProductService ps = SpringContextHolder.getBean(ProductService.class);
                //获取token
                String token = CacheUtil.getToken();
                String closeUrl = serviceInfo.getCloseUrl();
                JSONArray jsonArray = ps.queryProduct(token, serviceInfo.getProductId());
                if (null != jsonArray && jsonArray.size() > 0) {
                    //获取商品code
                    JSONObject product = jsonArray.getJSONObject(0);
                    closeUrl = product.get("closeInterface").toString();//获取关闭接口
                }
                //关闭服务
                CloseParam param = new CloseParam();
                param.setAppId(serviceInfo.getAppId());
                param.setOpenType(serviceInfo.getOpenType());

                ServiceInfo service = new ServiceInfo();
                try {
                    if (OpenType.Demand.getCode().equals(serviceInfo.getOpenType())) {
                        Charge[] charges = JSON.parseObject(serviceInfo.getChargeCodes(), Charge[].class);
                        param.setCharg(charges);
                    }
                    Result result = ReqUtil.operaProd(token, param, closeUrl);
                    if (null == result || !"success".equalsIgnoreCase(result.getState())){
                        service.setFailReason("关闭服务接口返回失败:"+JSON.toJSONString(result, SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty));
                    }
                } catch (Exception e) {
                    log.error("关闭服务失败:", e);
                    service.setFailReason("关闭服务抛出异常:"+e.getMessage());
                }
                service.setUuid(serviceInfo.getUuid());
                service.setServiceStatus(ServiceStatus.Closed.getCode());
                //服务14天后销毁
                service.setDestroyDate(DateUtils.addDays(serviceInfo.getExpireDate(),14));
                updates.add(service);
                //发送消息
                MsgUtil msgUtil = SpringContextHolder.getBean(MsgUtil.class);
                msgUtil.sendMsg(token, MsgSubType.ServiceCloseNotice.getCode(), serviceInfo.getSiteCode(),serviceInfo.getAccountId(), serviceInfo.getProductId(), serviceInfo.getServiceNo(), "","","");
            }
            if (updates.size() > 0) {
                //更新状态
                sis.updateBatchById(updates);
//                //销毁倒计时
//                for (ServiceInfo update : updates) {
//                    destroyServicePublisher.publishMsg(update.getUuid());
//                }
            }
        }
    }
}
