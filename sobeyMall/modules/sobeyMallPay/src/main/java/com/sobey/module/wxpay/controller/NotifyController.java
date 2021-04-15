package com.sobey.module.wxpay.controller;

import com.github.wxpay.sdk.WXPayUtil;
import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.wxpay.common.WXPayConstant;
import com.sobey.module.wxpay.config.WXPayConf;
import com.sobey.module.wxpay.entity.notify.Notify;
import com.sobey.module.wxpay.service.HandleWxCallbackService;
import com.sobey.module.wxpay.utils.EntityMapUtil;
import com.sobey.module.wxpay.utils.SignUtil;
import com.sobey.module.wxpay.utils.SortUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 支付通知相关
 * @Author WuChenYang
 * @Since 2020/1/15 11:17
 */
@Controller
@RequestMapping("/notification")
@ApiIgnore
public class NotifyController {

    private static final Logger log = LoggerFactory.getLogger(NotifyController.class);

    @Autowired
    private WXPayConf wxPayConf;
    @Autowired
    private HandleWxCallbackService hwcs;

    /**
     * 统一下单支付异步通知
     */
    @PostMapping("/wx")
    @PassToken
    public void notifyOfPay(HttpServletRequest request, HttpServletResponse response) {
        //获取通知内容
        try {
            String xml = getXmlBody(request);
            log.info("收到微信支付回调参数:" + xml);
            Map<String, String> map = WXPayUtil.xmlToMap(xml);
            //校验参数  这里用微信的工具类把xml解析成map，因为微信没有对map的value去空格，所以需要手动去除
            // 包括后边将map转成实体类时也是需要手动去除空格
            String original_sign = map.remove("sign").trim();
            //排序
            List<Map.Entry<String, String>> entries = SortUtil.sortParam(map);
            String data = SignUtil.spliceParam(entries, "&");
            //签名
            data = data + "&key=" + wxPayConf.getKey();
            String sign = SignUtil.sign(data, wxPayConf.getKey(), WXPayConstant.HMAC_SHA256);
            //校验签名
            if (!original_sign.equals(sign)) {
                String resultXml = WXPayUtil.mapToXml(getRespBody("FAIL", "签名失败"));
                response.setCharacterEncoding("UTF-8");
                response.setContentType(MediaType.APPLICATION_XML_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(resultXml);
                writer.close();
                return;
            }
            Notify notify = EntityMapUtil.mapToEntity(map, Notify.class);
            if (null == notify) {
                String resultXml = WXPayUtil.mapToXml(getRespBody("FAIL", "未能成功解析数据"));
                response.setCharacterEncoding("UTF-8");
                response.setContentType(MediaType.APPLICATION_XML_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(resultXml);
                writer.close();
                return;
            }
            //异步处理数据
            hwcs.handleData(notify);
            String resultXml = WXPayUtil.mapToXml(getRespBody("SUCCESS", "OK"));
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_XML_VALUE);
            PrintWriter writer = response.getWriter();
            writer.write(resultXml);
            writer.close();
        } catch (Exception e) {
            log.error("微信支付回调解析异常", e);
        }

    }

    /**
     * 统一下单支付异步通知(处理仅支付接口的回调)
     */
    @PostMapping("/onlyPay")
    @PassToken
    public void onlyPayNotice(HttpServletRequest request, HttpServletResponse response) {

        try {
            //解析参数
            String xml = getXmlBody(request);
            log.info("收到微信支付回调参数:" + xml);
            Map<String, String> map = WXPayUtil.xmlToMap(xml);
            //校验参数
            String original_sign = map.remove("sign").trim();
            //排序
            List<Map.Entry<String, String>> entries = SortUtil.sortParam(map);
            String data = SignUtil.spliceParam(entries, "&");
            //签名
            data = data + "&key=" + wxPayConf.getKey();
            String sign = SignUtil.sign(data, wxPayConf.getKey(), WXPayConstant.HMAC_SHA256);
            //校验签名
            if (!original_sign.equals(sign)) {
                String resultXml = WXPayUtil.mapToXml(getRespBody("FAIL", "签名失败"));
                response.setCharacterEncoding("UTF-8");
                response.setContentType(MediaType.APPLICATION_XML_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(resultXml);
                writer.close();
                return;
            }
            Notify notify = EntityMapUtil.mapToEntity(map, Notify.class);
            if (null == notify) {
                String resultXml = WXPayUtil.mapToXml(getRespBody("FAIL", "未能成功解析数据"));
                response.setCharacterEncoding("UTF-8");
                response.setContentType(MediaType.APPLICATION_XML_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(resultXml);
                writer.close();
                return;
            }
            //异步处理数据
            hwcs.handleOnlyPayNotice(notify);
            String resultXml = WXPayUtil.mapToXml(getRespBody("SUCCESS", "OK"));
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_XML_VALUE);
            PrintWriter writer = response.getWriter();
            writer.write(resultXml);
            writer.close();
        } catch (Exception e) {
            log.error("微信支付回调解析异常", e);
        }
    }

    /**
     * 获取xml参数
     *
     * @param request
     * @return
     */
    private String getXmlBody(HttpServletRequest request) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        String line = "";
        StringBuilder builder = new StringBuilder();
        while ((line = br.readLine()) != null) {
            builder.append(line);
        }
        br.close();
        //解析参数
        return builder.toString();
    }

    /**
     * 生成返回给微信的响应体
     *
     * @param return_code
     * @param return_msg
     * @return
     */
    private Map<String, String> getRespBody(String return_code, String return_msg) {
        Map<String, String> map = new HashMap<>();
        map.put("return_code", return_code);
        map.put("return_msg", return_msg);
        return map;
    }

}
