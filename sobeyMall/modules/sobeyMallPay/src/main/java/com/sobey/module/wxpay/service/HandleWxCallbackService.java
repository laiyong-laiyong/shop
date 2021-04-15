package com.sobey.module.wxpay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.balance.model.Balance;
import com.sobey.module.balance.service.BalanceService;
import com.sobey.module.common.enumeration.EffectiveDurationUnitType;
import com.sobey.module.common.enumeration.IsEffective;
import com.sobey.module.common.util.BalUtil;
import com.sobey.module.common.util.DateUtil;
import com.sobey.module.common.util.MsgUtil;
import com.sobey.module.common.util.NoticePackUtil;
import com.sobey.module.fegin.mallPack.pack.entity.MallPack;
import com.sobey.module.fegin.mallPack.pack.service.MallPackService;
import com.sobey.module.fegin.mallPack.packOrder.entity.request.MallPackOrder;
import com.sobey.module.fegin.mallPack.packOrder.entity.request.MallPackResource;
import com.sobey.module.fegin.mallPack.packOrder.service.PackOrderService;
import com.sobey.module.fegin.msg.enumeration.MsgSubType;
import com.sobey.module.fegin.order.entity.response.Order;
import com.sobey.module.fegin.order.service.OrderService;
import com.sobey.module.fegin.serviceInfo.entity.response.ResultInfo;
import com.sobey.module.fegin.serviceInfo.entity.response.ServiceInfo;
import com.sobey.module.fegin.serviceInfo.service.ServiceInfoService;
import com.sobey.module.fegin.user.service.UserService;
import com.sobey.module.order.OrderType;
import com.sobey.module.order.ServiceStatus;
import com.sobey.module.pay.PayConstant;
import com.sobey.module.pay.PayStatus;
import com.sobey.module.wxpay.entity.notify.Notify;
import com.sobey.module.wxpay.entity.onlypay.BeingResp;
import com.sobey.module.wxpay.entity.onlypay.OnlyPayNotify;
import com.sobey.module.wxpay.utils.CacheUtil;
import com.sobey.util.common.appid.AppIdUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.SSLContext;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WCY
 * @createTime 2020/5/11 14:42
 * 处理微信支付回调的数据
 */
@Component
public class HandleWxCallbackService {

    private static final Logger log = LoggerFactory.getLogger(HandleWxCallbackService.class);

    @Autowired
    private OrderService os;
    @Autowired
    private MallPackService mps;
    @Autowired
    private ServiceInfoService sis;
    @Autowired
    private AppIdUtil appIdUtil;
    @Autowired
    private NoticePackUtil noticePackUtil;
    @Autowired
    private MsgUtil msgUtil;
    @Autowired
    private BalanceService bs;
    @Autowired
    private PackOrderService pos;
    @Autowired
    private UserService us;

    /**
     * 处理微信支付回调的数据
     *
     * @param notify
     */
    @Async("executors")
    public void handleData(Notify notify) {
        String attach = notify.getAttach();
        if ("pack".equalsIgnoreCase(attach)) {
            handlePackOrderPayNotice(notify);
            return;
        }
        try {
            String result_code = notify.getResult_code();
            String orderNo = notify.getOut_trade_no();//订单号
            String token = CacheUtil.getToken();
            Order order = os.findByOrderNo(token, orderNo);
            if (PayStatus.Paid.getCode().equals(order.getPaymentStatus())) {
                return;
            }
            Date date = new Date();
            //交易成功
            if ("SUCCESS".equals(result_code)) {
                BigDecimal payAmount = order.getDiscountPrice();
                //支付成功更新数据
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("payDate", DateUtil.format(date, DateUtil.FORMAT_1));
                orderMap.put("payAmount", payAmount);
                orderMap.put("paymentStatus", PayStatus.Paid.getCode());
                orderMap.put("payMethod", PayConstant.PayMethod.WXPay.getCode());

                //判断是充值还是消费
                BigDecimal balanceAmount = BigDecimal.valueOf(0.00);
                Balance balance = null;
                ServiceInfo serviceInfo = null;
                if (PayConstant.TransType.RECHARGE.getCode().equals(order.getTransType())) {
                    Map<String, Object> bsMap = new HashMap<>();
                    bsMap.put("accountId", order.getAccountId());
                    List<Balance> balances = bs.selectByMap(bsMap);
                    balance = balances.get(0);
                    //存放余额中的原始金额
                    BigDecimal original = balance.getBalance();

                    balanceAmount = original.add(payAmount);
                    log.info("账户" + order.getAccountId() + "进行微信充值,充值前金额:" + original + ",充值后金额:" + balanceAmount);

                    //这里通知服务模块扫描
                    sis.finishRecharge(token, balanceAmount.doubleValue() > 0, balance.getAccountId());
                    //这里新建对象不进行全量更新
                    Balance bal = new Balance();
                    bal.setUuid(balance.getUuid());
                    bal.setBalance(balanceAmount);
                    bal.setSign(BalUtil.encodeBal(balanceAmount));
                    balance = bal;
                }
                if (PayConstant.TransType.CONSUMPTION.getCode().equals(order.getTransType())) {
                    //新购
                    if (OrderType.New.getCode().equals(order.getOrderType())) {
                        log.info("微信支付成功,商品" + order.getProductId() + "新购" + order.getDuration() + "个月,正在处理中...");
                        serviceInfo = new ServiceInfo();
                        String appId = appIdUtil.getAppIdMD5(order.getAccountId(), order.getProductId(), order.getVersionId());
                        String serviceNo = IdWorker.getIdStr();
                        serviceInfo.setUuid(IdWorker.get32UUID())
                                .setAppId(appId)
                                .setServiceNo(serviceNo)
                                .setAccountId(order.getAccountId())
                                .setAccount(order.getAccount())
                                .setOpenType(order.getOpenType())
                                .setChargeCodes(order.getChargeCodes())
                                .setProductId(order.getProductId())
                                .setProductSpecs(order.getProductSpecs())
                                .setOpenUrl(order.getOpenUrl())
                                .setRenewUrl(order.getRenewUrl())
                                .setCloseUrl(order.getCloseUrl())
                                .setCreateDate(date)
                                .setExpireDate(DateUtils.addDays(date, order.getDuration()))
                                .setVersionId(order.getVersionId())
                                .setSpecifications(order.getSpecifications())
                                .setSiteCode(order.getSiteCode())
                                .setRelatedProductId(order.getRelatedProductId())
                                .setRelatedProductName(order.getRelatedProductName());
                        orderMap.put("serviceNo", serviceNo);//更新订单中的服务号
                    }
                    if (OrderType.Renew.getCode().equals(order.getOrderType())) {
                        log.info("微信支付成功,商品" + order.getProductId() + "续费" + order.getDuration() + "个月,正在处理中...");
                        serviceInfo = sis.select(token, order.getServiceNo());
                        Date expireDate = serviceInfo.getExpireDate();
                        //服务是否是已过期处于关闭状态
                        if (ServiceStatus.Closed.getCode().equals(serviceInfo.getServiceStatus())) {
                            expireDate = date;
                        }
                        serviceInfo.setExpireDate(DateUtils.addDays(expireDate, order.getDuration()));
                        serviceInfo.setUpdateDate(date);
                    }
                }

                //更新数据
                log.info("微信支付成功,更新订单号为" + orderNo + "的订单的相关信息");
                update(token, order, orderMap, balance, serviceInfo);
                //充值发送消息
                if (PayConstant.TransType.RECHARGE.getCode().equals(order.getTransType())) {
                    msgUtil.sendMsg(token, MsgSubType.RechargeNotice.getCode(), order.getSiteCode(), order.getAccountId(), "", "", payAmount.setScale(2, BigDecimal.ROUND_DOWN).toString(), balanceAmount.setScale(2, BigDecimal.ROUND_DOWN).toString(), "");
                }
                if (PayConstant.TransType.CONSUMPTION.getCode().equals(order.getTransType())) {
                    if (OrderType.Renew.getCode().equals(order.getOrderType())) {
                        msgUtil.sendMsg(token, MsgSubType.ServiceRenewNotice.getCode(), order.getSiteCode(), order.getAccountId(), order.getProductId(), serviceInfo.getServiceNo(), order.getDiscountPrice().setScale(2, BigDecimal.ROUND_DOWN).toString(), "", "");
                    }
                }
            }
            //交易失败
            if ("FAIL".equals(result_code)) {
                Map<String, Object> failMap = new HashMap<>();
                failMap.put("paymentStatus", PayStatus.Failure.getCode());
                os.update(token, order.getId(), failMap);
            }
        } catch (Exception e) {
            log.error("处理微信支付回调数据时发生异常", e);
        }
    }

    /**
     * 处理支付套餐包商品时的回调
     *
     * @param notify
     */
    public void handlePackOrderPayNotice(Notify notify) {

        String result_code = notify.getResult_code();
        String orderNo = notify.getOut_trade_no();//订单号
        String token = CacheUtil.getToken();
        Date date = new Date();
        if ("SUCCESS".equalsIgnoreCase(result_code)) {
            MallPackOrder packOrder = pos.findByOrderNo(token, orderNo);
            //支付成功不重复处理
            if (PayStatus.Paid.getCode().equals(packOrder.getPayStatus())) {
                return;
            }
            //创建套餐包
            MallPack mallPack = new MallPack();
            String uuid = IdWorker.get32UUID();
            BeanUtils.copyProperties(packOrder, mallPack);
            mallPack.setEffectiveDate(date);
            mallPack.setIsEffective(IsEffective.Effective.getCode());
            Date expireDate = EffectiveDurationUnitType.getExpireDate(packOrder.getUnit(), packOrder.getDuration(), date);
            mallPack.setExpireDate(expireDate);
            mallPack.setCreateDate(date);
            mallPack.setUuid(uuid);
            List<MallPackResource> resources = mallPack.getResources();
            for (MallPackResource resource : resources) {
                resource.setCreateDate(date);
                resource.setRemainingSize(resource.getSize());
                resource.setMallPackId(uuid);
                resource.setUuid(IdWorker.get32UUID());
                resource.setExpireDate(expireDate);
            }
            mallPack.setResources(resources);
            //更新订单信息
            Map<String, Object> update = new HashMap<>();
            update.put("payAmount", packOrder.getDiscountPrice());
            update.put("payStatus", PayStatus.Paid.getCode());
            update.put("payDate", DateUtil.format(date, DateUtil.FORMAT_1));
            update.put("payMethod", PayConstant.PayMethod.WXPay.getCode());
            updatePackData(token, packOrder.getId(), update, mallPack);
            //发送套餐包购买通知
            noticePackUtil.noticePack(token, packOrder.getPackUuid(), packOrder.getPackName(), packOrder.getProductId(), packOrder.getAccountId(), packOrder.getSiteCode(), expireDate, resources);
        }

    }

    /**
     * 套餐包支付完后处理数据
     *
     * @param token
     * @param orderId
     * @param update
     * @param mallPack
     */
    @Transactional
    public void updatePackData(String token,
                               String orderId,
                               Map<String, Object> update,
                               MallPack mallPack) {
        pos.update(token, orderId, update);
        mps.createPack(token, mallPack);
    }

    /**
     * 处理第三方调用微信支付回调的数据
     *
     * @param notify
     */
    @Async("executors")
    public void handleOnlyPayNotice(Notify notify) {

        try {
            String resultCode = notify.getReturn_code();
            OnlyPayNotify onlyPayNotify = new OnlyPayNotify();
            onlyPayNotify.setResultCode(resultCode);
            onlyPayNotify.setAttach(notify.getAttach());
            onlyPayNotify.setErrorMsg(StringUtils.isBlank(notify.getReturn_msg()) ? notify.getErr_code_des() : notify.getReturn_msg());
            onlyPayNotify.setOrderNo(notify.getOut_trade_no());
            onlyPayNotify.setTotalFee(notify.getTotal_fee());
            String noticeUrl = CacheUtil.remove(notify.getOut_trade_no());
            CloseableHttpClient httpClient = HttpClients.createDefault();
            //是否是https
            if (StringUtils.startsWithIgnoreCase(noticeUrl, "https")) {
                SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();
                SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslContext);
                httpClient = HttpClients.custom().setSSLSocketFactory(factory).build();
            }

            HttpPost httpPost = new HttpPost(noticeUrl);
            StringEntity entity = new StringEntity(JSON.toJSONString(onlyPayNotify, SerializerFeature.WriteNullStringAsEmpty), StandardCharsets.UTF_8);
            log.info("微信支付回调第三方接口:URL-" + noticeUrl + ",Param-" + JSON.toJSONString(onlyPayNotify, SerializerFeature.WriteNullStringAsEmpty));
            httpPost.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            httpPost.setEntity(entity);
            //失败重试次数
            int num = 5;
            requestNoticeUrl(httpClient, httpPost, num);
        } catch (Exception e) {
            log.error("处理微信支付回调数据异常", e);
        }
    }

    /**
     * 请求第三方回调地址，通知微信支付结果
     *
     * @param httpClient
     * @param request
     * @return
     */
    private void requestNoticeUrl(CloseableHttpClient httpClient, HttpUriRequest request, int num) {
        try {
            while (num >= 1) {
                CloseableHttpResponse response = httpClient.execute(request);
                //注:EntityUtils.toString()方法调用一次就会自动销毁，所以不能重复使用该方法
                String respEntity = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                log.info("调用外部接口返回:" + respEntity);
                BeingResp beingResp = JSON.parseObject(respEntity, BeingResp.class);
                String result = beingResp.getExtend_message().get("result");
                if ("SUCCESS".equalsIgnoreCase(result)) {
                    return;
                }
                //15s重试一次
                Thread.sleep(15000);
                num--;
                log.info("重试剩余次数:" + num);
            }
        } catch (Exception e) {
            log.info("调用第三方回调地址发生异常:" + e);
        }
    }

    /**
     * 更新数据
     *
     * @param token
     * @param order
     * @param orderMap
     * @param balance
     * @param service
     */
    public void update(String token, Order order, Map<String, Object> orderMap, Balance balance, ServiceInfo service) {
        if (null != balance) {
            bs.updateById(balance);
        }
        String id = order.getId();
        String orderType = order.getOrderType();
        os.update(token, id, orderMap);
        if (PayConstant.TransType.CONSUMPTION.getCode().equals(order.getTransType())) {
            if (OrderType.New.getCode().equals(orderType)) {
                ResultInfo result = sis.createService(token, service);
                if (!"SUCCESS".equalsIgnoreCase(result.getCode())) {
                    throw new AppException(ExceptionType.ORDER_CODE_OPEN_FAIL, "商品开通失败", new RuntimeException());
                }
            }
            if (OrderType.Renew.getCode().equals(orderType)) {
                ResultInfo result = sis.renew(token, service);
                if (!"SUCCESS".equalsIgnoreCase(result.getCode())) {
                    throw new AppException(ExceptionType.ORDER_CODE_RENEW_FAIL, "商品续费失败", new RuntimeException());
                }
            }
        }

    }

}
