package com.sobey.module.productservice.controller;

/**
 * @author WCY
 * @createTime 2020/6/16 11:27
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.module.fegin.msg.request.service.MsgService;
import com.sobey.module.fegin.user.response.UserDetail;
import com.sobey.module.fegin.user.service.UserService;
import com.sobey.module.order.ServiceStatus;
import com.sobey.module.order.entity.ResultInfo;
import com.sobey.module.productservice.model.ServiceInfo;
import com.sobey.module.productservice.service.ServiceInfoService;
import com.sobey.module.utils.CacheUtil;
import com.sobey.util.business.header.HeaderUtil;
import com.sobey.util.common.appid.AppIdUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 开通saas
 */
@RestController
@RequestMapping("/${api.v1}/saas")
public class OpenSAASController {

    private static final Logger log = LoggerFactory.getLogger(OpenSAASController.class);

    @Autowired
    private AppIdUtil appIdUtil;
    @Autowired
    private ServiceInfoService sis;
    @Autowired
    private MsgService msgService;
    @Autowired
    private UserService userService;

    @PostMapping("/open")
    public ResultInfo open(@RequestBody ServiceInfo serviceInfo) {

        try {
            String openUrl = serviceInfo.getOpenUrl();
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(openUrl);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            log.info("开通saas返回:"+result);
            JSONObject jsonObject = JSON.parseObject(result, JSONObject.class);

            String serviceNo = IdWorker.getIdStr();
            //生成appId
            String appId = appIdUtil.getAppIdMD5(serviceInfo.getAccountId(), serviceInfo.getProductId(), serviceInfo.getVersionId());

            //创建时间
            Date createDate = new Date();
            serviceInfo.setServiceNo(serviceNo);
            serviceInfo.setUuid(IdWorker.get32UUID());
            serviceInfo.setCreateDate(createDate);
            serviceInfo.setAppId(appId);

            if ("success".equals(jsonObject.getString("state"))) {
                String token = HeaderUtil.getAuth();
                serviceInfo.setServiceStatus(ServiceStatus.Normal.getCode());
                UserDetail userDetail = userService.userDetail(serviceInfo.getSiteCode(),serviceInfo.getAccountId(), CacheUtil.getToken(),"sobeyLingYunMall");
                String login_name = userDetail.getLogin_name();
                Map<String,String> msg = new HashMap<>();
                msg.put("accountId",serviceInfo.getAccountId());
                msg.put("msg","【服务已开通】您好！"+login_name+"，您的融合媒体服务产品已开通！地址:"+jsonObject.getString("url")+",用户名:test,密码:test");
                msgService.sendSaasMsg(token,msg);
                sis.insert(serviceInfo);
                return ResultInfo.withSuccess("开通成功");
            } else {
                serviceInfo.setServiceStatus(ServiceStatus.OpenFail.getCode());
            }
            sis.insert(serviceInfo);
        } catch (IOException e) {
            return ResultInfo.withFail("开通异常");

        }
        return ResultInfo.withSuccess("开通失败");
    }

}
