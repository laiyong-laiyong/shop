package com.sobey.module.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.exception.ExceptionKit;
import com.sobey.module.fegin.balance.request.BalanceService;
import com.sobey.module.fegin.balance.response.Balance;
import com.sobey.module.fegin.msg.enumeration.MsgSubType;
import com.sobey.module.fegin.product.request.service.DiscountService;
import com.sobey.module.fegin.product.request.service.ProductService;
import com.sobey.module.fegin.product.response.entity.Metric;
import com.sobey.module.fegin.voucher.request.service.VouAccountService;
import com.sobey.module.fegin.voucher.response.entity.VoucherAccount;
import com.sobey.module.mallPack.model.MallPack;
import com.sobey.module.mallPack.model.MallPackResource;
import com.sobey.module.mallPack.model.MallPackUseRecord;
import com.sobey.module.mallPack.service.MallPackResourceService;
import com.sobey.module.mallPack.service.MallPackService;
import com.sobey.module.mq.producer.ChargeProducer;
import com.sobey.module.order.ServiceStatus;
import com.sobey.module.order.common.OpenType;
import com.sobey.module.order.entity.Result;
import com.sobey.module.order.model.Order;
import com.sobey.module.pay.PayConstant;
import com.sobey.module.pay.PayStatus;
import com.sobey.module.productservice.entity.charge.Charge;
import com.sobey.module.productservice.entity.charge.Charging;
import com.sobey.module.productservice.entity.charge.CloseParam;
import com.sobey.module.productservice.entity.charge.Usage;
import com.sobey.module.productservice.model.ConsumeFailMsg;
import com.sobey.module.productservice.model.MetricUsage;
import com.sobey.module.productservice.model.ServiceInfo;
import com.sobey.module.productservice.model.UsageStatistics;
import com.sobey.module.mq.constant.BindingConstant;
import com.sobey.module.productservice.service.*;
import com.sobey.module.utils.BalUtil;
import com.sobey.module.utils.MsgUtil;
import com.sobey.module.utils.RedisLock;
import com.sobey.module.utils.ReqUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/18 17:05
 */
@Component
public class ChargeConsumer {

    private static final Logger log = LoggerFactory.getLogger(ChargeConsumer.class);

    @Autowired
    private ProductService ps;
    @Autowired
    private ConsumeFailMsgService cfms;
    @Autowired
    private BalanceService bs;
    @Autowired
    private ServiceInfoService sis;
    @Autowired
    private TransactionService ts;
    @Autowired
    private MsgUtil msgUtil;
    @Autowired
    private MallPackResourceService mprs;
    @Autowired
    private MallPackService mps;
    @Autowired
    private UsageStatisticsService uss;
    @Autowired
    private VouAccountService vas;
    @Autowired
    private DiscountService ds;
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private ChargeProducer producer;

    @RabbitListener(bindings =
    @QueueBinding(value = @Queue(value = BindingConstant.CHARGE_QUEUE, durable = "true"),
            exchange = @Exchange(value = BindingConstant.CHARGE_DIRECT_EXCHANGE),
            key = BindingConstant.ROUTING_KEY), ackMode = "AUTO")
    @RabbitHandler
    public void receive(Message msg) {
        if (null == msg) {
            log.info("收到队列" + BindingConstant.CHARGE_QUEUE + "的消息为null");
            return;
        }
        Charging charging = null;
        String lockKey = "";
        try {
            String message = new String(msg.getBody(), StandardCharsets.UTF_8);
            if (StringUtils.isBlank(message)) {
                log.info("消息体为空");
                return;
            }
            log.info("收到队列" + BindingConstant.CHARGE_QUEUE + "的消息:" + message);
            charging = JSON.parseObject(message, Charging.class);
            //处理数据
            String requestId = charging.getRequestId();
            String appId = charging.getAppId();
            Map<String, Object> param = new HashMap<>();
            param.put("requestId", requestId);
            List<UsageStatistics> usageList = uss.selectByMap(param);
            if (null != usageList && usageList.size() > 0) {
                log.info("requestId为" + requestId + "的计费信息已经处理过，无需重复处理");
                return;
            }
            Usage[] usages = charging.getUsage();
            Map<String, Object> headers = msg.getMessageProperties().getHeaders();
            String token = headers.getOrDefault("token", "").toString();
            //查询服务信息
            ServiceInfo serviceInfo = sis.findByAppId(appId);
            if (null == serviceInfo) {
                log.info("未查询到appId为" + appId + "的服务信息");
                saveFailMsg(charging, "未查询到正常的服务信息,请检查是否开通了该服务", usages);
                return;
            }
            String siteCode = serviceInfo.getSiteCode();
            String accountId = serviceInfo.getAccountId();
            String productId = serviceInfo.getProductId();
            String account = serviceInfo.getAccount();
            charging.setUserCode(accountId);
            //以accountId为锁
            lockKey = accountId;
            if (!redisLock.lock(lockKey, "charge", 20000)) {
                producer.publishChargeMsg(charging);
                return;
            }
            Date date = new Date();
            //首先判断是否有套餐包
            List<MallPackUseRecord> useRecords = new ArrayList<>();//记录使用明细
            List<MallPackResource> updateResources = new ArrayList<>(); //记录要更新的套餐包信息
            List<Usage> updateUsages = new ArrayList<>();//记录套餐包不足时需要余额支付的部分以及没有对应的metricId的套餐资源的部分
            MallPack mallPack = new MallPack();
            mallPack.setAccountId(accountId);
            mallPack.setProductId(productId);
            List<MallPack> mallPacks = mps.list(mallPack);
            if (null != mallPacks && mallPacks.size() > 0) {
                for (Usage usage : usages) {
                    String metricId = usage.getId();
                    BigDecimal value = BigDecimal.valueOf(usage.getValue());
                    /**
                     * C这里是没有找到按流量计费的信息
                     */
                    List<MallPackResource> packResources = mprs.queryEffective(accountId, productId, metricId);
                    if (null == packResources || packResources.size() == 0) {
                        updateUsages.add(usage);
                        continue;
                    }
                    //该资源对应的套餐包 同一个metricId只可能属于同一个套餐包
                    MallPack pack = mallPacks.stream().filter(mp -> mp.getUuid().equals(packResources.get(0).getMallPackId())).collect(Collectors.toList()).get(0);

                    //如果是无限量套餐 与商品那边约定为size是-1为无限量套餐包
                    if ((BigDecimal.valueOf(-1).compareTo(packResources.get(0).getSize())) == 0) {
                        MallPackUseRecord useRecord = new MallPackUseRecord();
                        BeanUtils.copyProperties(pack, useRecord);
                        BeanUtils.copyProperties(packResources.get(0), useRecord);
                        useRecord.setId(IdWorker.get32UUID());
                        useRecord.setConsumptionDate(date);
                        useRecord.setUsageAmount(value);
                        useRecord.setBeforeUsageAmount(BigDecimal.valueOf(-1.0));
                        useRecord.setAfterUsageAmount(BigDecimal.valueOf(-1.0));
                        useRecords.add(useRecord);
                    } else {

                        //套餐包资源查询结果是按照失效时间升序排序,优先使用最先失效的
                        //统计对应次单价id的套餐包包含的资源规格总量
                        BigDecimal total = BigDecimal.valueOf(0.0000);
                        for (MallPackResource resource : packResources) {
                            total = total.add(resource.getRemainingSize());
                        }
                        //当套餐包足以支付
                        if (total.compareTo(value) >= 0) {
                            for (MallPackResource resource : packResources) {
                                BigDecimal size = resource.getRemainingSize();
                                if (null == size || (value.doubleValue() == 0)) {
                                    continue;
                                }
                                size = resource.getRemainingSize().subtract(value);
                                MallPackUseRecord useRecord = new MallPackUseRecord();
                                BeanUtils.copyProperties(pack, useRecord);
                                BeanUtils.copyProperties(resource, useRecord);
                                useRecord.setId(IdWorker.get32UUID());
                                useRecord.setConsumptionDate(date);
                                useRecord.setUsageAmount(value);
                                useRecord.setBeforeUsageAmount(resource.getRemainingSize());
                                //当size >= 0时可知截止到当前的资源已经足以支付使用量
                                if (size.doubleValue() >= 0) {
                                    useRecord.setAfterUsageAmount(size);
                                    useRecords.add(useRecord);

                                    resource.setRemainingSize(size);
                                    updateResources.add(resource);
                                    break;
                                }
                                //如果小于0则说明当前资源不足，需要用到下一个资源支付
                                useRecord.setAfterUsageAmount(BigDecimal.valueOf(0).setScale(4, RoundingMode.DOWN));
                                useRecords.add(useRecord);

                                resource.setRemainingSize(BigDecimal.valueOf(0).setScale(4, RoundingMode.DOWN));
                                updateResources.add(resource);
                                /**
                                 * 假设有三个资源，分别是2，3，20。value的初始值是20，
                                 * 所有第一次是-18，所有要取这个值正数，用来下次减。
                                 * 
                                 */
                                value = size.negate();
                            }
                            //这里是进入下一次循环
                            continue;
                        }
                        //如果剩余套餐不足以支付则需要使用余额支付剩余部分
                        usage.setValue(value.subtract(total).doubleValue());
                        updateUsages.add(usage);
                        for (MallPackResource resource : packResources) {
                            MallPackUseRecord useRecord = new MallPackUseRecord();
                            BeanUtils.copyProperties(pack, useRecord);
                            BeanUtils.copyProperties(resource, useRecord);
                            useRecord.setId(IdWorker.get32UUID());
                            useRecord.setConsumptionDate(date);
                            useRecord.setUsageAmount(resource.getRemainingSize());
                            useRecord.setBeforeUsageAmount(resource.getRemainingSize());
                            useRecord.setAfterUsageAmount(BigDecimal.valueOf(0.0000));
                            useRecords.add(useRecord);

                            resource.setRemainingSize(BigDecimal.valueOf(0.0000));
                            updateResources.add(resource);
                        }
                    }
                }
            }
            //
            if (useRecords.size() > 0) {
                if (updateUsages.size() > 0) {
                    handlePack(token, charging, updateUsages, updateResources, useRecords);
                } else {
                    handlePack(token, charging, null, updateResources, useRecords);
                }
            } else {
                //查询折扣
                BigDecimal discount = ds.queryDiscount(token, accountId, productId);
                if (null == discount) {
                    discount = BigDecimal.valueOf(1.00);
                }
                //没有流量包则查询单价计费
                BigDecimal price = BigDecimal.valueOf(0.0000);//计价
                BigDecimal lmiPri = BigDecimal.valueOf(0.0000);//统计限价
                //查单价计算费用
                List<MetricUsage> metricUsages = new ArrayList<>();
                String orderNo = IdWorker.getIdStr();//订单号
                for (Usage usage : usages) {
                    MetricUsage metricUsage = new MetricUsage();
                    String code = usage.getId();
                    double value = usage.getValue();
                    if (0 == value) {
                        log.info("单价id为" + code + "的使用量为0");
                        continue;
                    }
                    List<Metric> metrics = ps.queryPrice(token, code, null);
                    if (null == metrics || metrics.size() == 0) {
                        log.info("未查询到id为" + code + "的单价信息");
                        saveFailMsg(charging, "未查询到单价信息", new Usage[]{usage});
                        continue;
                    }
                    Metric metric = metrics.get(0);
                    //保存用量信息
                    metricUsage.setUuid(IdWorker.get32UUID());
                    metricUsage.setCreateTime(date)
                            .setRequestId(requestId)
                            .setOrderNo(orderNo)
                            .setPriceId(code)
                            .setPrice(metric.getPrice())
                            .setTypeCode(metric.getType())
                            .setType(metric.getName())
                            .setValue(value);
                    metricUsages.add(metricUsage);

                    BigDecimal basicPrice = metric.getPrice();
                    if (null == basicPrice || 0 == basicPrice.doubleValue()) {
                        log.info("查询到id为" + code + "的单价为0");
                        continue;
                    }
                    lmiPri = lmiPri.add(metric.getLimitedPrice().multiply(BigDecimal.valueOf(value)));
                    price = price.add(basicPrice.multiply(BigDecimal.valueOf(value)));
                }
                if (metricUsages.size() == 0) {
                    log.info("未能查到单价信息或用量为0");
                    saveFailMsg(charging, "未能查到单价信息或用量为0", usages);
                    return;
                }

                //创建订单实体
                Order order = new Order();
                order.setId(IdWorker.get32UUID())
                        .setServiceNo(serviceInfo.getServiceNo())
                        .setPaymentStatus(PayStatus.Paid.getCode())
                        .setAccountId(accountId)
                        .setAccount(account)
                        .setOrderNo(orderNo)
                        .setTransType(PayConstant.TransType.CONSUMPTION.getCode())
                        .setChargeCodes(serviceInfo.getChargeCodes())
                        .setPayDate(date)
                        .setCreateDate(date)
                        .setOpenType(serviceInfo.getOpenType())
                        .setCloseUrl(serviceInfo.getCloseUrl())
                        .setOpenUrl(serviceInfo.getOpenUrl())
                        .setRenewUrl(serviceInfo.getRenewUrl())
                        .setProductId(productId)
                        .setProductSpecs(serviceInfo.getProductSpecs())
                        .setVersionId(serviceInfo.getVersionId())
                        .setLimPri(lmiPri)
                        .setOrderAmount(price.setScale(8, BigDecimal.ROUND_DOWN))
                        .setSiteCode(siteCode);

                //判断是否能用代金券代金券
                JSONArray jsonArray = ps.queryProduct(token, serviceInfo.getProductId());
                JSONObject product = jsonArray.getJSONObject(0);
                VoucherAccount updateVouAccount = null;
                if ("1".equals(product.getString("voucher"))) {
                    VoucherAccount voucherAccount = vas.detail(token, accountId);
                    if (null != voucherAccount) {
                        BigDecimal vouAmount = voucherAccount.getVouAmount();
                        //如果代金券能一次支付则使用代金券，否则使用余额
                        if (vouAmount.compareTo(price) >= 0) {
                            updateVouAccount = new VoucherAccount();
                            updateVouAccount.setUuid(voucherAccount.getUuid());
                            vouAmount = vouAmount.subtract(price);
                            updateVouAccount.setVouAmount(vouAmount);
                            updateVouAccount.setUpdateTime(date);
                            order.setPayMethod(PayConstant.PayMethod.Voucher.getCode());
                            order.setPayAmount(order.getOrderAmount());
                            order.setDiscount(BigDecimal.valueOf(1.0));
                            order.setDiscountPrice(order.getOrderAmount());
                            //更新数据
                            ts.newOrder(order, metricUsages, new ArrayList<>(), null, token, charging, updateVouAccount);
                            return;
                        }

                    }
                }
                //计算折扣后的价格
                BigDecimal discountPrice = price.multiply(discount).setScale(8, RoundingMode.DOWN);
                order.setDiscount(discount);
                order.setDiscountPrice(discountPrice);
                order.setPayAmount(discountPrice);
                //查询余额
                Balance balance = bs.query(token, accountId);
                if (null == balance) {
                    log.info("未在余额表中查询到userCode为" + accountId + "的用户信息,请检查userCode是否正确");
                    saveFailMsg(charging, "未在余额表中查询到用户信息,请检查userCode是否正确", usages);
                    return;
                }
                BigDecimal credits = balance.getCredits();//信用额度
                if (null == credits) {
                    credits = BigDecimal.valueOf(0.00);
                }
                //是否第一次欠费
                boolean isFirstArrears = true;
                if (balance.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                    isFirstArrears = false;
                }
                BigDecimal bal = balance.getBalance().subtract(discountPrice);
                balance.setBalance(bal);
                balance.setSign(BalUtil.encodeBal(bal));
                order.setPayMethod(PayConstant.PayMethod.Balance.getCode());

                //判断是否需要关闭商品
                List<ServiceInfo> updateList = new ArrayList<>();
                if (bal.compareTo(BigDecimal.ZERO) < 0) {
                    if (isFirstArrears) {
                        //发送欠费通知
                        msgUtil.sendMsg(token, MsgSubType.ArrearsNotice.getCode(), siteCode, accountId, "", serviceInfo.getServiceNo(), "", bal.setScale(2, BigDecimal.ROUND_DOWN).toString(), credits.toString());
                        //发送欠费通知给运维人员
                        msgUtil.sendNoticeToAdmin(token, siteCode, accountId, bal.toString(), credits.toString());
                    }
                    //信用额度用完后关闭所有服务
                    if (bal.add(credits).compareTo(BigDecimal.ZERO) <= 0) {
                        //发送信用额度用完提醒(有信用额度才发送)
                        if (credits.compareTo(BigDecimal.ZERO) > 0) {
                            msgUtil.sendMsg(token, MsgSubType.CreditsNotice.getCode(), siteCode, accountId, "", "", "", "", credits.toString());
                            msgUtil.sendNoticeToCustom(token, siteCode, accountId);
                        }
                        //查询该用户所有的正常按量服务
                        Map<String, Object> serviceMap = new HashMap<>();
                        serviceMap.put("accountId", accountId);
                        serviceMap.put("serviceStatus", ServiceStatus.Normal.getCode());
                        serviceMap.put("openType", OpenType.Demand.getCode());
                        List<ServiceInfo> list = sis.selectByMap(serviceMap);
                        for (ServiceInfo service : list) {
                            try {
                                //关闭商品
                                String closeUrl = product.getString("closeInterface");//获取关闭接口
                                Charge[] charges = JSON.parseObject(service.getChargeCodes(), Charge[].class);
                                CloseParam close = new CloseParam();
                                if (null != charges && charges.length > 0) {
                                    Charge[] charg = new Charge[charges.length];
                                    for (int i = 0; i < charg.length; i++) {
                                        Charge charge = new Charge();
                                        charge.setId(charges[i].getId());
                                        charg[i] = charge;
                                    }
                                    close.setCharg(charg);
                                }
                                close.setAppId(service.getAppId());
                                close.setOpenType(service.getOpenType());
                                close.setUserCode(accountId);
                                Result result = ReqUtil.operaProd(token, close, closeUrl);
                                if (null == result || !"success".equalsIgnoreCase(result.getState())) {
                                    log.info("通知关闭商品失败,info:" + JSON.toJSONString(result) + ",请人工通知卖家");
                                }
                                //无论请求是否成功都要关闭商品
                                ServiceInfo info = new ServiceInfo();
                                info.setServiceStatus(ServiceStatus.Closed.getCode());
                                info.setUuid(service.getUuid());
                                info.setUpdateDate(date);
                                updateList.add(info);
                            } catch (Exception e) {
                                log.info("服务(appId:{}),关闭异常", service.getAppId());
                                ServiceInfo info = new ServiceInfo();
                                info.setServiceStatus(ServiceStatus.Closed.getCode());
                                info.setUuid(service.getUuid());
                                info.setUpdateDate(date);
                                info.setFailReason("关闭异常:" + e.getMessage());
                                updateList.add(info);
                            }
                        }
                    }
                }
                balance.setUpdateDate(date);
                //更新信息
                ts.newOrder(order, metricUsages, updateList, balance, token, charging, null);
            }
        } catch (Exception e) {
            log.error("计费异常:" + e);
            //保存消息
            saveFailMsg(charging, ExceptionKit.toString(e), charging.getUsage());
        } finally {
            redisLock.unlock(lockKey);
        }

    }

    public void saveFailMsg(Charging charging, String failReason, Usage[] usages) {
        ConsumeFailMsg failMsg = new ConsumeFailMsg();
        failMsg.setAppId(charging.getAppId());
        failMsg.setUuid(IdWorker.getIdStr());
        failMsg.setUserCode(charging.getUserCode());
        failMsg.setUsage(JSON.toJSONString(usages));
        failMsg.setFailReason(failReason);
        failMsg.setCreateDate(new Date());
        failMsg.setManualProcessStatus("0");
        cfms.insert(failMsg);
    }

    /**
     * 处理套餐抵扣
     *
     * @param token
     * @param charging
     * @param updateUsages
     * @param updateResources
     */
    public void handlePack(String token, Charging charging, List<Usage> updateUsages, List<MallPackResource> updateResources, List<MallPackUseRecord> useRecords) {
        try {
            if (null == updateUsages) {
                //更新套餐包信息并且创建使用明细
                ts.handleChargeInfo(token, null, null, null, new ArrayList<>(), updateResources, useRecords, charging, null);
                return;
            }

            Date date = new Date();
            String requestId = charging.getRequestId();
            ServiceInfo serviceInfo = sis.findByAppId(charging.getAppId());
            //查询折扣
            BigDecimal discount = ds.queryDiscount(token, serviceInfo.getAccountId(), serviceInfo.getProductId());
            if (null == discount) {
                discount = BigDecimal.valueOf(1.00);
            }
            BigDecimal price = BigDecimal.valueOf(0.0000);//计价
            BigDecimal lmiPri = BigDecimal.valueOf(0.0000);//统计限价
            //查单价计算费用
            List<MetricUsage> metricUsages = new ArrayList<>();
            String orderNo = IdWorker.getIdStr();//订单号
            for (Usage usage : updateUsages) {
                MetricUsage metricUsage = new MetricUsage();
                String code = usage.getId();
                double value = usage.getValue();
                if (0 == value) {
                    log.info("单价id为" + code + "的使用量为0");
                    continue;
                }
                List<Metric> metrics = ps.queryPrice(token, code, null);
                if (null == metrics || metrics.size() == 0) {
                    log.info("未查询到id为" + code + "的单价信息");
                    saveFailMsg(charging, "未查询到单价信息", new Usage[]{usage});
                    continue;
                }
                Metric metric = metrics.get(0);
                //保存用量信息
                metricUsage.setUuid(IdWorker.get32UUID());
                metricUsage.setCreateTime(date)
                        .setRequestId(requestId)
                        .setOrderNo(orderNo)
                        .setPriceId(code)
                        .setPrice(metric.getPrice())
                        .setTypeCode(metric.getType())
                        .setType(metric.getName())
                        .setValue(value);
                metricUsages.add(metricUsage);
                BigDecimal basicPrice = metric.getPrice();
                if (null == basicPrice || 0 == basicPrice.doubleValue()) {
                    log.info("查询到id为" + code + "的单价为0");
                    continue;
                }
                lmiPri = lmiPri.add(metric.getLimitedPrice().multiply(BigDecimal.valueOf(value)));
                price = price.add(basicPrice.multiply(BigDecimal.valueOf(value)));
            }
            if (metricUsages.size() == 0) {
                log.info("未能查到单价信息或用量为0");
                saveFailMsg(charging, "未能查到单价信息或用量为0", charging.getUsage());
                return;
            }
            String accountId = serviceInfo.getAccountId();
            String siteCode = serviceInfo.getSiteCode();
            //创建订单实体
            Order order = new Order();
            order.setId(IdWorker.get32UUID())
                    .setServiceNo(serviceInfo.getServiceNo())
                    .setPaymentStatus(PayStatus.Paid.getCode())
                    .setAccountId(accountId)
                    .setAccount(serviceInfo.getAccount())
                    .setOrderNo(orderNo)
                    .setTransType(PayConstant.TransType.CONSUMPTION.getCode())
                    .setChargeCodes(serviceInfo.getChargeCodes())
                    .setPayDate(date)
                    .setCreateDate(date)
                    .setOpenType(serviceInfo.getOpenType())
                    .setCloseUrl(serviceInfo.getCloseUrl())
                    .setOpenUrl(serviceInfo.getOpenUrl())
                    .setRenewUrl(serviceInfo.getRenewUrl())
                    .setProductId(serviceInfo.getProductId())
                    .setProductSpecs(serviceInfo.getProductSpecs())
                    .setVersionId(serviceInfo.getVersionId())
                    .setOrderAmount(price.setScale(8, BigDecimal.ROUND_DOWN))
                    .setLimPri(lmiPri)
                    .setSiteCode(siteCode);
            //判断是否能用代金券代金券
            JSONArray jsonArray = ps.queryProduct(token, serviceInfo.getProductId());
            Object o = jsonArray.get(0);
            String str = JSON.toJSONString(o);
            JSONObject product = JSON.parseObject(str, JSONObject.class);
            VoucherAccount updateVouAccount = null;
            if ("1".equals(product.getString("voucher"))) {
                VoucherAccount voucherAccount = vas.detail(token, accountId);
                if (null != voucherAccount && voucherAccount.getVouAmount().doubleValue() > 0) {
                    BigDecimal vouAmount = voucherAccount.getVouAmount();
                    //如果代金券不足以支付
                    if (vouAmount.compareTo(price) >= 0) {
                        updateVouAccount = new VoucherAccount();
                        updateVouAccount.setUuid(voucherAccount.getUuid());
                        vouAmount = vouAmount.subtract(price);
                        updateVouAccount.setVouAmount(vouAmount);
                        order.setPayMethod(PayConstant.PayMethod.Voucher.getCode());
                        order.setPayAmount(order.getOrderAmount());
                        order.setDiscount(BigDecimal.valueOf(1.0));
                        order.setDiscountPrice(order.getOrderAmount());
                        //更新数据
                        ts.handleChargeInfo(token, order, metricUsages, null, new ArrayList<>(), updateResources, useRecords, charging, updateVouAccount);
                        return;
                    }

                }
            }
            //计算折扣后价格
            BigDecimal discountPrice = price.multiply(discount).setScale(8, RoundingMode.DOWN);
            order.setDiscount(discount);
            order.setDiscountPrice(discountPrice);
            order.setPayAmount(discountPrice);
            //查询余额
            Balance balance = bs.query(token, accountId);
            if (null == balance) {
                log.info("未在余额表中查询到userCode为" + accountId + "的用户信息,请检查userCode是否正确");
                saveFailMsg(charging, "未在余额表中查询到用户信息,请检查userCode是否正确", charging.getUsage());
                return;
            }
            BigDecimal credits = balance.getCredits();
            if (null == credits) {
                credits = BigDecimal.valueOf(0.00);
            }
            //判断是否第一次欠费
            boolean isFirstArrears = true;
            if (balance.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                isFirstArrears = false;
            }
            BigDecimal bal = balance.getBalance().subtract(discountPrice);
            balance.setBalance(bal);
            balance.setSign(BalUtil.encodeBal(bal));
            order.setPayMethod(PayConstant.PayMethod.Balance.getCode());
            //判断是否需要关闭商品
            List<ServiceInfo> updateList = new ArrayList<>();
            if (bal.compareTo(BigDecimal.ZERO) < 0) {
                if (isFirstArrears) {
                    //发送欠费通知
                    msgUtil.sendMsg(token, MsgSubType.ArrearsNotice.getCode(), siteCode, accountId, "", serviceInfo.getServiceNo(), "", bal.setScale(2, BigDecimal.ROUND_DOWN).toString(), credits.toString());
                    //发送欠费通知给运维人员
                    msgUtil.sendNoticeToAdmin(token, siteCode, accountId, bal.toString(), credits.toString());
                }
                //如果所欠费用超过了信用额度
                if (bal.add(credits).compareTo(BigDecimal.ZERO) <= 0) {
                    //发送信用额度用完提醒(有信用额度才发送)
                    if (credits.compareTo(BigDecimal.ZERO) > 0) {
                        msgUtil.sendMsg(token, MsgSubType.CreditsNotice.getCode(), siteCode, accountId, "", "", "", "", credits.toString());
                        msgUtil.sendNoticeToCustom(token, siteCode, accountId);
                    }
                    //查询该用户所有的正常服务
                    Map<String, Object> serviceMap = new HashMap<>();
                    serviceMap.put("accountId", accountId);
                    serviceMap.put("serviceStatus", ServiceStatus.Normal.getCode());
                    serviceMap.put("openType", OpenType.Demand.getCode());
                    List<ServiceInfo> list = sis.selectByMap(serviceMap);
                    for (ServiceInfo service : list) {
                        //关闭商品
                        String closeUrl = product.getString("closeInterface");//获取关闭接口
                        Charge[] charges = JSON.parseObject(service.getChargeCodes(), Charge[].class);
                        CloseParam close = new CloseParam();
                        if (null != charges && charges.length > 0) {
                            Charge[] charg = new Charge[charges.length];
                            for (int i = 0; i < charg.length; i++) {
                                Charge charge = new Charge();
                                charge.setId(charges[i].getId());
                                charg[i] = charge;
                            }
                            close.setCharg(charg);
                        }
                        close.setAppId(service.getAppId());
                        close.setOpenType(service.getOpenType());
                        close.setUserCode(accountId);
                        Result result = ReqUtil.operaProd(token, close, closeUrl);
                        if (null == result || !"success".equalsIgnoreCase(result.getState())) {
                            log.info("通知关闭商品失败,info:" + JSON.toJSONString(result) + ",请人工通知卖家");
                        }
                        //无论请求是否成功都要关闭商品
                        ServiceInfo info = new ServiceInfo();
                        info.setServiceStatus(ServiceStatus.Closed.getCode());
                        info.setUuid(service.getUuid());
                        info.setUpdateDate(date);
                        updateList.add(info);
                    }
                }
            }
            balance.setUpdateDate(date);
            ts.handleChargeInfo(token, order, metricUsages, balance, updateList, updateResources, useRecords, charging, null);
        } catch (Exception e) {
            log.error("计费异常:" + e);
            //保存消息
            saveFailMsg(charging, ExceptionKit.toString(e), charging.getUsage());
        }

    }

}
