package com.sobey.module.token.fegin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sobey.framework.config.address.Auth;
import com.sobey.module.token.fegin.entity.RequestTokenParam;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WCY
 * @createTime 2020/4/28 9:58
 * 获取token
 */
@Component
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Value("${auth.grant_type}")
    private String grant_type;
    @Value("${auth.client_id}")
    private String client_id;
    @Value("${auth.client_secret}")
    private String client_secret;
    @Autowired
    private Auth auth;
    

    public JSONObject getToken() {
        String authUrl = auth.getUrl()+ "/oauth/token";
        log.info("authUrl = "+authUrl);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(authUrl);
        RequestTokenParam param = new RequestTokenParam();
        param.setClient_id(client_id);
        param.setClient_secret(client_secret);
        param.setGrant_type(grant_type);
        Field[] fields = param.getClass().getDeclaredFields();
        List<NameValuePair> list = new ArrayList<>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if ("serialVersionUID".equalsIgnoreCase(field.getName())) {
                    continue;
                }
                list.add(new BasicNameValuePair(field.getName(), field.get(param).toString()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String respStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            return JSON.parseObject(respStr, JSONObject.class);
        } catch (Exception e) {
            log.info("获取token异常",e);
            throw new RuntimeException(e);
        }
    }


}
