package com.sobey.module.zfbpay.controller;

import com.sobey.framework.jwt.annotation.PassToken;
import com.sobey.module.fegin.mallPack.packOrder.entity.request.MallPackOrder;
import com.sobey.module.fegin.mallPack.packOrder.service.PackOrderService;
import com.sobey.module.fegin.order.entity.response.Order;
import com.sobey.module.fegin.order.service.OrderService;
import com.sobey.module.fegin.product.request.service.ProductService;
import com.sobey.module.pay.PayStatus;
import com.sobey.module.voucher.service.VoucherAccountService;
import com.sobey.module.wxpay.utils.CacheUtil;
import com.sobey.module.zfbpay.config.Alipay;
import com.sobey.module.zfbpay.entity.tradePage.response.TradePagePayCallBack;
import com.sobey.module.zfbpay.service.HandleZfbCallbackService;
import com.sobey.module.zfbpay.service.ZfbService;
import com.sobey.util.business.header.HeaderUtil;
import com.sobey.util.common.json.JsonKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;

/**
 *
 */
@RestController
@RequestMapping("${api.v1}/alipay")
@Api(value = "支付宝支付", description = "支付宝支付")
public class ZfbController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ZfbService zfb;
    @Autowired
    private OrderService os;
    @Autowired
    private Alipay alipay;
    @Autowired
    private VoucherAccountService vas;
    @Autowired
    private HandleZfbCallbackService hcs;
    @Autowired
    private PackOrderService pos;
    @Autowired
    private ProductService ps;

    @ApiOperation(value = "统一收单线下交易预创建,此接口不用")
    @PostMapping("/precreate")
    public void precreate(@RequestParam(required = true) @ApiParam("订单号") String outTradeNo,
                          @RequestParam(required = true) @ApiParam("订单总金额") BigDecimal totalAmount,
                          @RequestParam(required = true) @ApiParam("订单标题") String subject) {
        this.zfb.precreate(outTradeNo, totalAmount, subject);

    }

    @ApiOperation(value = "商品统一收单下单并支付页面接口,此接口返回是form表单,直接放在html页面中就可以展示支付码")
    @PostMapping("/trade-page-pay")
    public String tradePagePay(@RequestParam @ApiParam("订单号") String outTradeNo,
                               @RequestParam @ApiParam("订单总金额") BigDecimal totalAmount,
                               @RequestParam @ApiParam("订单标题") String subject,
                               @RequestParam(required = false) @ApiParam("同步回调通知地址,可以是页面,也可以是接口。如果是接口需要是get方式") String returnUrl,
                               @RequestParam(required = false) @ApiParam("异步回调通知地址,可以是页面,也可以是接口。如果是接口需要是post方式") String notifyUrl) {

        String token = HeaderUtil.getAuth();
        Order order = os.findByOrderNo(token, outTradeNo);
        if (null == order) {
            return "订单号不存在";
        }
        //判断是否已失效
        if (PayStatus.Expired.getCode().equals(order.getPaymentStatus())){
            return "订单已失效,请重新下单";
        }
        totalAmount = order.getDiscountPrice();
//        Map<String, Object> map = new HashMap<>();
//        map.put("paymentStatus", PayStatus.Payments.getCode());
//        os.update(token, order.getId(), map);

        return zfb.tradePagePay(outTradeNo, totalAmount.setScale(2,RoundingMode.DOWN), subject, "normal", returnUrl, alipay.getNotify_url());
    }

    @PassToken
    @ApiOperation(value = "同步回调地址测试，不需要token")
    @GetMapping("/callback/sync")
    public void callbackSync(HttpServletRequest rq) {
        String out_trade_no = rq.getParameter("out_trade_no");
    }

    /**
     * 套餐包支付接口
     *
     * @param outTradeNo
     * @param totalAmount
     * @param subject
     * @param returnUrl
     * @param notifyUrl
     * @return
     */
    @ApiOperation(value = "套餐包统一收单下单并支付页面接口,此接口返回是form表单,直接放在html页面中就可以展示支付码", httpMethod = "POST")
    @PostMapping("/trade-page-pay/pack")
    public String tradePagePayPack(@RequestParam @ApiParam("订单号") String outTradeNo,
                                   @RequestParam @ApiParam("支付金额") BigDecimal totalAmount,
                                   @RequestParam @ApiParam("订单标题") String subject,
                                   @RequestParam(required = false) @ApiParam("同步回调通知地址,可以是页面,也可以是接口。如果是接口需要是get方式") String returnUrl,
                                   @RequestParam(required = false) @ApiParam("异步回调通知地址,可以是页面,也可以是接口。如果是接口需要是post方式") String notifyUrl) {

        //修改订单状态为支付中
        String token = HeaderUtil.getAuth();
        MallPackOrder order = pos.findByOrderNo(token, outTradeNo);
        if (null == order) {
            return "订单号不存在";
        }
        //判断是否已失效
        if (PayStatus.Expired.getCode().equals(order.getPayStatus())){
            return "订单已失效,请重新下单";
        }
        totalAmount = order.getDiscountPrice();

//        Map<String, Object> map = new HashMap<>();
//        map.put("paymentStatus", PayStatus.Payments.getCode());
//        pos.update(token, order.getId(), map);
        String passBackParams = "pack";
        try {
            passBackParams = URLDecoder.decode(passBackParams, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("编码错误", e);
            return "系统异常";
        }
        return zfb.tradePagePay(outTradeNo, totalAmount.setScale(2,RoundingMode.DOWN), subject, passBackParams, returnUrl, alipay.getNotify_url());
    }

    /**
     * 支付宝支付异步回调
     *
     * @param rq
     */
    @PassToken
    @ApiOperation(value = "异步回调地址测试，不需要token")
    @PostMapping("/callback/async")
    public void callbackSync(TradePagePayCallBack rq, HttpServletResponse rp) {
        log.info("收到支付宝支付异步回调通知" + JsonKit.beanToJson(rq));

        String passback_params = rq.getPassback_params();
        String out_trade_no = rq.getOut_trade_no();
        String token = CacheUtil.getToken();
        try {
            passback_params = StringUtils.isBlank(passback_params) ? "" : URLDecoder.decode(passback_params, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //这种情况不可能出现
            log.error("编码错误", e);
        }
        if ("pack".equalsIgnoreCase(passback_params)) {
            MallPackOrder order = pos.findByOrderNo(token, out_trade_no);
            hcs.handleCallBackOfPack(token, order);
            return;
        }
        Order order = os.findByOrderNo(token, out_trade_no);
        hcs.handleCallback(token, order);
        /**
         * 这里是阿里要求这样返回
         *
         */
        try {
            rp.getWriter().print("success");
        } catch (IOException e) {
            log.error("返回阿里报错", e);
        }
    }


}
