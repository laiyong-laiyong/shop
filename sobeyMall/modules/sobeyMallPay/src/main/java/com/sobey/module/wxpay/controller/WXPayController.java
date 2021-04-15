package com.sobey.module.wxpay.controller;

import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.common.util.IpUtil;
import com.sobey.module.fegin.mallPack.packOrder.entity.request.MallPackOrder;
import com.sobey.module.fegin.mallPack.packOrder.service.PackOrderService;
import com.sobey.module.fegin.order.entity.response.Order;
import com.sobey.module.fegin.order.service.OrderService;
import com.sobey.module.fegin.product.request.service.ProductService;
import com.sobey.module.pay.PayStatus;
import com.sobey.module.voucher.service.VoucherAccountService;
import com.sobey.module.wxpay.common.WXPayConstant;
import com.sobey.module.wxpay.config.WXPayConf;
import com.sobey.module.wxpay.entity.onlypay.OnlyPay;
import com.sobey.module.wxpay.entity.onlypay.ResEntity;
import com.sobey.module.wxpay.entity.orderQuery.OrderQuery;
import com.sobey.module.wxpay.entity.orderQuery.Result;
import com.sobey.module.wxpay.entity.unifiedorder.ResultBody;
import com.sobey.module.wxpay.entity.unifiedorder.UnifiedOrder;
import com.sobey.module.wxpay.utils.*;
import com.sobey.util.business.header.HeaderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @Since 2020/2/6 11:14
 */
@RestController
@RequestMapping("/${api.v1}/pay")
@Api(value = "微信支付接口", description = "微信支付接口")
public class WXPayController {

    private static final Logger log = LoggerFactory.getLogger(WXPayController.class);

    @Autowired
    private OrderService os;
    @Autowired
    private WXPayConf wxPayConf;
    @Autowired
    private VoucherAccountService vas;
    @Autowired
    private PackOrderService pos;
    @Autowired
    private ProductService ps;

    @PostMapping("/wx")
    @ApiOperation(value = "微信支付", httpMethod = "POST")
    public ResultBody wxPay(@RequestBody UnifiedOrder unifiedOrder,
                            HttpServletRequest request) {

        ResultBody result = new ResultBody();
        String token = HeaderUtil.getAuth();
        if (null == unifiedOrder) {
            result.setReturn_code("FAIL");
            result.setReturn_msg("参数错误");
            return result;
        }
        String orderNo = unifiedOrder.getOut_trade_no();
        Order order = os.findByOrderNo(token, orderNo);

        if (null == order) {
            result.setReturn_code("FAIL");
            result.setReturn_msg("订单号:" + orderNo + "不存在");
            return result;
        }
        //判断是否已失效
        if (PayStatus.Expired.getCode().equals(order.getPaymentStatus())){
            result.setReturn_code("FAIL");
            result.setReturn_msg("订单已失效,请重新下单");
            return result;
        }
        int totalFee = order.getDiscountPrice().multiply(BigDecimal.valueOf(100)).intValue();
        unifiedOrder.setTotal_fee(totalFee);
        setUnifiedOrder(unifiedOrder, request);
        //设置回调通知地址
        unifiedOrder.setNotify_url(wxPayConf.getNoticeUrl());

        //签名
        String data = SignUtil.spliceParam(
                SortUtil.sortParam(
                        EntityMapUtil.entityToMap(unifiedOrder)//实体类转map
                ),
                "&");//把排序完后的参数用 & 拼接
        data = data + "&key=" + wxPayConf.getKey();
        try {
            String sign = SignUtil.hmacSHA256(data, wxPayConf.getKey());
            unifiedOrder.setSign(sign);
            //统一下单
            ResultBody resultBody =
                    RequestUtil.unifiedOrder(EntityMapUtil.entityToMap(unifiedOrder), WXPayConstant.UNIFIED_ORDER_URL);

            if (null == resultBody) {
                result.setReturn_code("FAIL");
                result.setReturn_msg("调用微信支付失败");
                return result;
            }
//            if ("SUCCESS".equals(resultBody.getResult_code())) {
//                //更新订单状态为支付中
//                Map<String, Object> param = new HashMap<>();
//                param.put("paymentStatus", PayStatus.Payments.getCode());
//                os.update(token, order.getId(), param);
//            }
            return resultBody;
        } catch (Exception e) {
            throw new AppException(ExceptionType.PAY_CODE_WX_PAY, "微信支付异常", e);
        }

    }

    /**
     * 套餐包订单支付
     *
     * @param unifiedOrder
     * @param request
     * @return
     */
    @PostMapping("/wx/pack")
    @ApiOperation(value = "微信支付套餐包订单", httpMethod = "POST")
    public ResultBody wxPayPackOrder(@RequestBody UnifiedOrder unifiedOrder,
                                     HttpServletRequest request) {

        ResultBody result = new ResultBody();
        String token = HeaderUtil.getAuth();
        if (null == unifiedOrder) {
            result.setReturn_code("FAIL");
            result.setReturn_msg("参数错误");
            return result;
        }
        String orderNo = unifiedOrder.getOut_trade_no();
        MallPackOrder order = pos.findByOrderNo(token, orderNo);

        if (null == order) {
            result.setReturn_code("FAIL");
            result.setReturn_msg("订单号:" + orderNo + "不存在");
            return result;
        }
        //判断是否已失效
        if (PayStatus.Expired.getCode().equals(order.getPayStatus())){
            result.setReturn_code("FAIL");
            result.setReturn_msg("订单已失效,请重新下单");
            return result;
        }
        int totalFee = (int) (order.getDiscountPrice().doubleValue() * 100);
        unifiedOrder.setTotal_fee(totalFee);
        String accountId = order.getAccountId();
        setUnifiedOrder(unifiedOrder, request);
        unifiedOrder.setAttach("pack");//用来标识是套餐包订单
        //设置回调通知地址
        unifiedOrder.setNotify_url(wxPayConf.getNoticeUrl());
        //签名
        String data = SignUtil.spliceParam(
                SortUtil.sortParam(
                        EntityMapUtil.entityToMap(unifiedOrder)//实体类转map
                ),
                "&");//把排序完后的参数用 & 拼接
        data = data + "&key=" + wxPayConf.getKey();
        try {
            String sign = SignUtil.hmacSHA256(data, wxPayConf.getKey());
            unifiedOrder.setSign(sign);
            //统一下单
            ResultBody resultBody =
                    RequestUtil.unifiedOrder(EntityMapUtil.entityToMap(unifiedOrder), WXPayConstant.UNIFIED_ORDER_URL);

            if (null == resultBody) {
                result.setReturn_code("FAIL");
                result.setReturn_msg("调用微信支付失败");
                return result;
            }
//            if ("SUCCESS".equals(resultBody.getResult_code())) {
//                //更新订单状态为支付中
//                Map<String, Object> param = new HashMap<>();
//                param.put("payStatus", PayStatus.Payments.getCode());
//                pos.update(token, order.getId(), param);
//            }
            return resultBody;
        } catch (Exception e) {
            throw new AppException(ExceptionType.PAY_CODE_WX_PAY, "微信支付异常", e);
        }

    }

    /**
     * 设置微信统一下单公共参数
     *
     * @param unifiedOrder
     * @param request
     * @return
     */
    private UnifiedOrder setUnifiedOrder(@NotNull UnifiedOrder unifiedOrder, @NotNull HttpServletRequest request) {
        //设置币种
        unifiedOrder.setFee_type(WXPayConstant.CNY);
        //设置商户信息
        unifiedOrder.setMch_id(wxPayConf.getMchId());
        unifiedOrder.setAppid(wxPayConf.getAppid());
        //设置ip
        unifiedOrder.setSpbill_create_ip(IpUtil.getIpAddr(request));
        //设置随机字符串
        unifiedOrder.setNonce_str(SignUtil.getRandomChars(32));
        //设置签名类型
        unifiedOrder.setSign_type(WXPayConstant.HMAC_SHA256);

        //设置交易类型
        unifiedOrder.setTrade_type(WXPayConstant.TradeType.NATIVE.name());
        return unifiedOrder;
    }

    /**
     * 第三方接入微信支付
     *
     * @param onlyPay
     */
    @PostMapping(value = "/wx/onlyPay")
    @ApiOperation(value = "第三方接入微信支付", httpMethod = "POST")
    public ResEntity onlyPay(@RequestBody OnlyPay onlyPay, HttpServletRequest request) {
        //校验参数
        ResEntity resEntity = new ResEntity();
        try {
            Class<? extends OnlyPay> clazz = onlyPay.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                if (null != annotation) {
                    if (annotation.required()) {
                        Object o = field.get(onlyPay);
                        if (null == o || StringUtils.isBlank(o.toString())) {
                            resEntity.setResultCode("FAIL");
                            resEntity.setErrorMsg("参数" + field.getName() + "不能为空");
                            return resEntity;
                        }
                    }
                }
            }
            UnifiedOrder unifiedOrder = new UnifiedOrder();
            setUnifiedOrder(unifiedOrder, request);
            unifiedOrder.setAttach(onlyPay.getAttach());
            unifiedOrder.setBody(onlyPay.getBody());
            unifiedOrder.setOut_trade_no(onlyPay.getOrderNo());
            unifiedOrder.setProduct_id(onlyPay.getProductId());
            unifiedOrder.setTotal_fee(onlyPay.getTotalFee());
            unifiedOrder.setNotify_url(wxPayConf.getOnlyPayNoticeUrl());
            unifiedOrder.setOpenid(onlyPay.getOpenId());
            unifiedOrder.setTrade_type(WXPayConstant.TradeType.JSAPI.name());
            //签名
            String data = SignUtil.spliceParam(
                    SortUtil.sortParam(
                            EntityMapUtil.entityToMap(unifiedOrder)//实体类转map
                    ),
                    "&");//把排序完后的参数用 & 拼接
            data = data + "&key=" + wxPayConf.getKey();
            String sign = SignUtil.hmacSHA256(data, wxPayConf.getKey());
            unifiedOrder.setSign(sign);
            //统一下单
            ResultBody resultBody =
                    RequestUtil.unifiedOrder(EntityMapUtil.entityToMap(unifiedOrder), WXPayConstant.UNIFIED_ORDER_URL);
            if (null == resultBody) {
                resEntity.setResultCode("FAIL");
                resEntity.setErrorMsg("调用微信支付异常,响应体为空");
                return resEntity;
            }
            if (!"SUCCESS".equalsIgnoreCase(resultBody.getResult_code())) {
                resEntity.setResultCode("FAIL");
                resEntity.setErrorMsg(StringUtils.isBlank(resultBody.getErr_code_des()) ? (StringUtils.isBlank(resultBody.getReturn_msg()) ? "调用微信支付失败" : resultBody.getReturn_msg()) : resultBody.getErr_code_des());
                return resEntity;
            }
            resEntity.setResultCode("SUCCESS");
            resEntity.setCodeUrl(resultBody.getCode_url());
            resEntity.setPrePayId(resultBody.getPrepay_id());
            CacheUtil.put(onlyPay.getOrderNo(), onlyPay.getNoticeUrl());//将第三方的回调地址放入缓存
            return resEntity;
        } catch (Exception e) {
            log.error("微信支付异常", e);
            resEntity.setResultCode("FAIL");
            resEntity.setErrorMsg("微信支付异常:" + e.getMessage());
        }
        return resEntity;
    }

    /**
     * 微信支付结果查询
     *
     * @param orderNo
     */
    @GetMapping("/orderQuery/{orderNo}")
    @ApiOperation(value = "微信支付结果查询", httpMethod = "GET")
    public Result orderQuery(@PathVariable("orderNo") String orderNo) {
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setAppid(wxPayConf.getAppid());
        orderQuery.setMch_id(wxPayConf.getMchId());
        orderQuery.setOut_trade_no(orderNo);
        orderQuery.setNonce_str(SignUtil.getRandomChars(32));
        orderQuery.setSign_type(WXPayConstant.HMAC_SHA256);
        Result result = null;
        try {
            String data = SignUtil.spliceParam(
                    SortUtil.sortParam(
                            EntityMapUtil.entityToMap(orderQuery)//实体类转map
                    ),
                    "&");//把排序完后的参数用 & 拼接
            data = data + "&key=" + wxPayConf.getKey();
            String sign = SignUtil.hmacSHA256(data, wxPayConf.getKey());
            orderQuery.setSign(sign);
            result = RequestUtil.orderQuery(orderQuery, WXPayConstant.ORDER_QUERY_URL);
        } catch (Exception e) {
            log.error("查询微信支付异常:" + e);
            result = new Result();
            result.setResult_code("FAIL");
            result.setErr_code(ExceptionType.SYS_RUNTIME.getCode());
            result.setErr_code_des("微信支付查询出现异常:" + e.getMessage());
        }
        return result;
    }

}
