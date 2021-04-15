package com.sobey.module.zfbpay.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.zfbpay.config.Alipay;
import com.sobey.module.zfbpay.entity.precreate.request.PrecreateRequest;
import com.sobey.module.zfbpay.entity.tradePage.TradePagePay;
import com.sobey.module.common.util.ZfbUtil;
import com.sobey.util.common.json.JsonKit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author lgc
 * @date 2020年1月19日 上午10:34:11
 */
@Service
public class ZfbService {

	private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ZfbUtil zfb;
    @Autowired
    private Alipay ali;

    public void precreate(String out_trade_no, BigDecimal total_amount, String subject) {
        AlipayClient ct = zfb.getAlipayClient();
        AlipayTradePrecreateRequest rq = new AlipayTradePrecreateRequest();

        PrecreateRequest pre = new PrecreateRequest();
        pre.setOut_trade_no(out_trade_no);
        pre.setTotal_amount(total_amount);
        pre.setSubject(subject);

        rq.setBizContent(JsonKit.beanToJson(pre));
        rq.setReturnUrl(ali.getReturn_url());
        AlipayTradePrecreateResponse sp = null;
        try {
            sp = ct.certificateExecute(rq);
        } catch (AlipayApiException e) {
            throw new AppException(ExceptionType.PAY_CODE_ZFB_PAY, e);
        }
        if (sp.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }

    }

    public String tradePagePay(String out_trade_no, BigDecimal total_amount, String subject, String passback_params, String returnUrl
            , String notifyUrl) {
        AlipayClient ct = zfb.getAlipayClient();
        AlipayTradePagePayRequest rq = new AlipayTradePagePayRequest();

        TradePagePay pre = new TradePagePay();
        pre.setOut_trade_no(out_trade_no);
        pre.setTotal_amount(total_amount);
        pre.setProduct_code("FAST_INSTANT_TRADE_PAY");
        pre.setSubject(subject);


        rq.setBizContent(JsonKit.beanToJson(pre));
        if (StringUtils.isBlank(returnUrl)) {
            rq.setReturnUrl(ali.getReturn_url());
        } else {
            rq.setReturnUrl(returnUrl);
        }
        if (StringUtils.isNotBlank(notifyUrl)) {
            rq.setNotifyUrl(notifyUrl);
        }
        AlipayTradePagePayResponse sp = null;
        log.info("请求参数是：" + JsonKit.beanToJson(rq));
        try {
            /**
             * 这里必须使用pageExecute，不能使用certificateExcute
             *
             */
            sp = ct.pageExecute(rq);
        } catch (AlipayApiException e) {
            throw new AppException(ExceptionType.PAY_CODE_ZFB_PAY, e);
        }
        if (sp.isSuccess()) {
        	log.info("返回参数是：" + JsonKit.beanToJson(sp));
            String body = sp.getBody();
            return body;
        } else {
            return null;
        }

    }

}
