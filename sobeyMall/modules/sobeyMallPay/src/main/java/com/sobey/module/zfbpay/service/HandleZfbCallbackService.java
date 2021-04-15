package com.sobey.module.zfbpay.service;

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
import com.sobey.util.common.appid.AppIdUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WCY
 * @createTime 2020/5/6 18:33
 */
@Component
public class HandleZfbCallbackService {

    private static final Logger log = LoggerFactory.getLogger(HandleZfbCallbackService.class);

    @Autowired
    private BalanceService bs;
    @Autowired
    private OrderService os;
    @Autowired
    private AppIdUtil appIdUtil;
    @Autowired
    private ServiceInfoService sis;
    @Autowired
    private NoticePackUtil noticePackUtil;
    @Autowired
    private MsgUtil msgUtil;
    @Autowired
    private MallPackService mps;
    @Autowired
    private PackOrderService pos;
    @Autowired
    private UserService us;

    /**
     * 支付宝支付成功回调处理
     *
     * @param order
     */
    @Async("executors")
    public void handleCallback(String token, Order order) {

        if (PayStatus.Paid.getCode().equals(order.getPaymentStatus())) {
            return;
        }

        String transType = order.getTransType();
        String accountId = order.getAccountId();
        String siteCode = order.getSiteCode();
        String productId = order.getProductId();
        BigDecimal discountPrice = order.getDiscountPrice();
        Date date = new Date();
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("paymentStatus", PayStatus.Paid.getCode());
        orderMap.put("payAmount", discountPrice);
        orderMap.put("payDate", DateUtil.format(date, DateUtil.FORMAT_1));
        orderMap.put("payMethod", PayConstant.PayMethod.ZFBPay.getCode());
        //充值
        if (PayConstant.TransType.RECHARGE.getCode().equalsIgnoreCase(transType)) {
            Map<String, Object> map = new HashMap<>();
            map.put("accountId", accountId);
            Balance bal = null;
            List<Balance> list = bs.selectByMap(map);
            if (null != list && list.size() == 1) {
                bal = new Balance();
                Balance balance = list.get(0);
                bal.setUuid(balance.getUuid());
                bal.setUpdateDate(date);
                BigDecimal balAmount = balance.getBalance().add(discountPrice);
                bal.setBalance(balAmount);
                bal.setSign(BalUtil.encodeBal(balAmount));
                update(token, order.getId(), orderMap, bal, null, true);
                if (PayConstant.TransType.RECHARGE.getCode().equals(order.getTransType())) {
                    msgUtil.sendMsg(token, MsgSubType.RechargeNotice.getCode(), siteCode,accountId, "", "", discountPrice.toString(), bal.getBalance().setScale(2, BigDecimal.ROUND_DOWN).toString(), "");
                }
                //通知服务模块扫描关闭的按量服务然后重新开通
                sis.finishRecharge(token, balAmount.doubleValue() > 0, accountId);
            }
            return;
        }
        ServiceInfo serviceInfo = null;
        //消费
        if (PayConstant.TransType.CONSUMPTION.getCode().equalsIgnoreCase(transType)) {
            //判断订单类型是新购还是续费
            String orderType = order.getOrderType();
            boolean flag = true;//是否新购
            //新购
            if (OrderType.New.getCode().equals(orderType)) {
                log.info("支付宝支付成功,商品" + productId + "新购" + order.getDuration() + "个月,正在处理中...");
                serviceInfo = new ServiceInfo();
                String appId = appIdUtil.getAppIdMD5(accountId, productId, order.getVersionId());
                String serviceNo = IdWorker.getIdStr();
                serviceInfo.setUuid(IdWorker.get32UUID())
                        .setAppId(appId)
                        .setServiceNo(serviceNo)
                        .setAccountId(order.getAccountId())
                        .setAccount(order.getAccount())
                        .setOpenType(order.getOpenType())
                        .setChargeCodes(order.getChargeCodes())
                        .setProductId(productId)
                        .setProductSpecs(order.getProductSpecs())
                        .setOpenUrl(order.getOpenUrl())
                        .setRenewUrl(order.getRenewUrl())
                        .setCloseUrl(order.getCloseUrl())
                        .setCreateDate(date)
                        .setExpireDate(DateUtils.addDays(date, order.getDuration()))
                        .setVersionId(order.getVersionId())
                        .setSpecifications(order.getSpecifications())
                        .setSiteCode(siteCode)
                        .setRelatedProductId(order.getRelatedProductId())
                        .setRelatedProductName(order.getRelatedProductName());
                orderMap.put("serviceNo", serviceNo);//更新订单中的服务号
            }
            if (OrderType.Renew.getCode().equals(orderType)) {
                flag = false;
                log.info("支付宝支付成功,商品" + productId + "续费" + order.getDuration() + "个月,正在处理中...");
                serviceInfo = sis.select(token, order.getServiceNo());
                Date expireDate = serviceInfo.getExpireDate();
                //判断该服务是否已过期
                if (ServiceStatus.Closed.getCode().equals(serviceInfo.getServiceStatus())) {
                    expireDate = date;
                }
                serviceInfo.setExpireDate(DateUtils.addDays(expireDate, order.getDuration()));
                serviceInfo.setUpdateDate(date);
            }
            update(token, order.getId(), orderMap, null, serviceInfo, flag);
        }
        //发送消息
        if (PayConstant.TransType.CONSUMPTION.getCode().equals(order.getTransType())) {
            if (OrderType.Renew.getCode().equals(order.getOrderType())) {
                msgUtil.sendMsg(token, MsgSubType.ServiceRenewNotice.getCode(), siteCode,accountId, productId, serviceInfo.getServiceNo(), discountPrice.toString(), "", "");
            }
        }

    }

    /**
     * 处理购买套餐包的回调
     *
     * @param token
     * @param order
     */
    @Async("executors")
    public void handleCallBackOfPack(String token, MallPackOrder order) {
        Date date = new Date();
        //创建套餐包
        MallPack mallPack = new MallPack();
        String uuid = IdWorker.get32UUID();
        BeanUtils.copyProperties(order, mallPack);
        mallPack.setEffectiveDate(date);
        mallPack.setIsEffective(IsEffective.Effective.getCode());
        Date expireDate = EffectiveDurationUnitType.getExpireDate(order.getUnit(), order.getDuration(), date);
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
        update.put("payAmount", order.getDiscountPrice());
        update.put("payStatus", PayStatus.Paid.getCode());
        update.put("payDate", DateUtil.format(date, DateUtil.FORMAT_1));
        update.put("payMethod", PayConstant.PayMethod.ZFBPay.getCode());
        updatePackData(token, order.getId(), update, mallPack);
        //发送套餐包购买通知
        noticePackUtil.noticePack(token,order.getPackUuid(),order.getPackName(),order.getProductId(),order.getAccountId(),order.getSiteCode(),expireDate,resources);
    }

    /**
     * 套餐包支付完后处理数据
     *
     * @param token
     * @param orderId
     * @param update
     * @param mallPack
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePackData(String token,
                               String orderId,
                               Map<String, Object> update,
                               MallPack mallPack) {
        mps.createPack(token, mallPack);
        pos.update(token, orderId, update);
    }

    /**
     * @param orderId
     * @param updateOrder
     * @param bal
     * @param service
     * @param flag        是否新购
     */
    public void update(String token, String orderId, Map<String, Object> updateOrder, Balance bal, ServiceInfo service, boolean flag) {
        if (null != bal) {
            bs.updateById(bal);
        }
        os.update(token, orderId, updateOrder);
        if (null != service) {
            if (flag) {
                ResultInfo result = sis.createService(token, service);
                if (!"SUCCESS".equalsIgnoreCase(result.getCode())) {
                    throw new AppException(ExceptionType.ORDER_CODE_OPEN_FAIL, "商品开通失败", new RuntimeException());
                }
            } else {
                ResultInfo result = sis.renew(token, service);
                if (!"SUCCESS".equalsIgnoreCase(result.getCode())) {
                    throw new AppException(ExceptionType.ORDER_CODE_RENEW_FAIL, "商品续费失败", new RuntimeException());
                }
            }
        }

    }

}
