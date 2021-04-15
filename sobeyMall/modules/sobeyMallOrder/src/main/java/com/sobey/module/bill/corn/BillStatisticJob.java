package com.sobey.module.bill.corn;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.framework.spring.SpringContextHolder;
import com.sobey.module.bill.model.Bill;
import com.sobey.module.bill.model.BillDetail;
import com.sobey.module.bill.model.NewBillDetail;
import com.sobey.module.bill.model.PersonalBill;
import com.sobey.module.bill.service.BillService;
import com.sobey.module.fegin.product.request.service.ProductService;
import com.sobey.module.fegin.user.response.UserDetail;
import com.sobey.module.fegin.user.service.UserService;
import com.sobey.module.mallPack.model.MallPackOrder;
import com.sobey.module.mallPack.service.MallPackOrderService;
import com.sobey.module.order.model.Order;
import com.sobey.module.order.service.OrderService;
import com.sobey.module.pay.PayConstant;
import com.sobey.module.pay.PayStatus;
import com.sobey.module.salescustomer.model.Salescustomer;
import com.sobey.module.salescustomer.service.SalescustomerService;
import com.sobey.module.token.fegin.AuthService;
import com.sobey.module.utils.DateUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author WCY
 * @createTime 2020/7/28 11:05
 * 每月账单统计任务 定时任务设置为每月1号 00:00执行 0 0 0 1 * ?
 */
@Component
public class BillStatisticJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(BillStatisticJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
        calendar.setTime(now);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //获取上个月1号00:00:00的时间
        Date start = DateUtils.addMonths(calendar.getTime(), -1);
        //获取上个月月末的时间 23:59:59.999
        Date end = DateUtils.addMilliseconds(calendar.getTime(), -1);

        OrderService os = SpringContextHolder.getBean(OrderService.class);
        MallPackOrderService mpos = SpringContextHolder.getBean(MallPackOrderService.class);

        //统计商品订单
        Map<String, Object> param = new HashMap<>();
        param.put("start", start);
        param.put("end", end);
        param.put("paymentStatus", PayStatus.Paid.getCode());
        param.put("transType", PayConstant.TransType.CONSUMPTION.getCode());
        List<Order> orders = os.statisticBill(param);
        log.info("{}统计购买商品生成的订单查询结果:{}", DateUtil.format(start, "yyyy/MM"), JSON.toJSONString(orders, SerializerFeature.WriteNullStringAsEmpty));
        //统计套餐包订单
        param.put("payStatus", PayStatus.Paid.getCode());
        List<MallPackOrder> packOrders = mpos.statisticBill(param);
        log.info("{}统计购买套餐包生成的订单结果:{}", DateUtil.format(start, "yyyy/MM"), JSON.toJSONString(packOrders, SerializerFeature.WriteNullStringAsEmpty));
        ArrayList<BillDetail> billDetails = new ArrayList<>(128);
        ArrayList<NewBillDetail> newBillDetails = new ArrayList<>(256);
        Bill bill = new Bill();
        String billUuid = IdWorker.get32UUID();
        bill.setUuid(billUuid);

        ProductService ps = SpringContextHolder.getBean(ProductService.class);
        UserService us = SpringContextHolder.getBean(UserService.class);
        AuthService authService = SpringContextHolder.getBean(AuthService.class);
        SalescustomerService salescustomerService = SpringContextHolder.getBean(SalescustomerService.class);
        String token = authService.getToken().getString("access_token");
        HashMap<String, String> productMap = new HashMap<>(128);
        HashMap<String, String> userMap = new HashMap<>(128);
        HashMap<String, String> billingMethodMap = new HashMap<>(128);
        if (orders.size() > 0) {
            for (Order order : orders) {
                String siteCode = order.getSiteCode();
                String accountId = order.getAccountId();
                String productId = order.getProductId();
                BigDecimal totalOrderAmount = order.getOrderAmount();//订单总金额
                BigDecimal totalPayAmount = order.getPayAmount();//支付总金额
                BigDecimal totalLimPri = order.getLimPri();//限价总金额
                String payMethod = order.getPayMethod();
                boolean billDetailFlag = false;
                if (billDetails.size() > 0) {
                    List<BillDetail> list = billDetails.stream().filter(bDetail ->
                            accountId.equals(bDetail.getAccountId()) && productId.equals(bDetail.getProductId())
                    ).collect(Collectors.toList());
                    if (list.size() > 0) {
                        BillDetail detail = list.get(0);
                        detail.setTotalOrderAmount(detail.getTotalOrderAmount().add(totalOrderAmount));
                        detail.setTotalLmiPri(detail.getTotalLmiPri().add(totalLimPri));
                        detail.setTotalPayAmount(detail.getTotalPayAmount().add(totalPayAmount));
                        if (PayConstant.PayMethod.Balance.getCode().equals(payMethod)) {
                            detail.setBalAmount(detail.getBalAmount().add(totalPayAmount));
                        }
                        if (PayConstant.PayMethod.WXPay.getCode().equals(payMethod)) {
                            detail.setWxAmount(detail.getWxAmount().add(totalPayAmount));
                        }
                        if (PayConstant.PayMethod.ZFBPay.getCode().equals(payMethod)) {
                            detail.setZfbAmount(detail.getZfbAmount().add(totalPayAmount));
                        }
                        if (PayConstant.PayMethod.Voucher.getCode().equals(payMethod)) {
                            detail.setVoucherAmount(detail.getVoucherAmount().add(totalPayAmount));
                        }
                        billDetailFlag = true;
                    }
                }
                NewBillDetail newBillDetail = new NewBillDetail();
                newBillDetail.setUuid(IdWorker.get32UUID());
                newBillDetail.setAccountId(accountId);
                newBillDetail.setBillType(PayConstant.TransType.CONSUMPTION.getCode());
                newBillDetail.setProductId(productId);
                newBillDetail.setSiteCode(siteCode);
                newBillDetail.setPayMethod(payMethod);
                newBillDetail.setBillDate(DateUtil.format(start, "yyyy/MM"));
                newBillDetail.setTotalRefundAmount(BigDecimal.ZERO);
                newBillDetail.setTotalOrderAmount(totalOrderAmount == null ? BigDecimal.ZERO : totalOrderAmount);
                newBillDetail.setTotalPayAmount(totalPayAmount == null ? BigDecimal.ZERO : totalPayAmount);
                newBillDetail.setTotalLmiPri(totalLimPri == null ? BigDecimal.ZERO : totalLimPri);
                if (!productMap.containsKey(productId)) {
                    //查询商品信息
                    JSONArray jsonArray = ps.queryProduct(token, productId);
                    if (null != jsonArray && jsonArray.size() == 1) {
                        String productName = jsonArray.getJSONObject(0).getString("name");
                        String billingMethod = jsonArray.getJSONObject(0).getString("saleChannel");
                        productMap.put(productId, productName);
                        billingMethodMap.put(productId, billingMethod);
                    }
                }
                if (!userMap.containsKey(accountId)) {
                    //查询用户名
                    UserDetail userDetail = us.userDetail(siteCode, accountId, token,"sobeyLingYunMall");
                    if (null != userDetail) {
                        userMap.put(accountId, userDetail.getLogin_name());
                    }
                }
                newBillDetail.setProductName(productMap.get(productId));
                newBillDetail.setBillingMethod(billingMethodMap.get(productId));
                newBillDetail.setAccount(userMap.get(accountId));
                newBillDetail.setCreateDate(now);
                newBillDetails.add(newBillDetail);

                if (!billDetailFlag) {
                    BillDetail billDetail = new BillDetail();
                    billDetail.setUuid(IdWorker.get32UUID());
                    billDetail.setBillUuid(billUuid);
                    billDetail.setAccountId(accountId);
                    billDetail.setSiteCode(siteCode);
                    billDetail.setBillType(PayConstant.TransType.CONSUMPTION.getCode());
                    billDetail.setProductId(productId);
                    billDetail.setTotalLmiPri(totalLimPri);
                    billDetail.setTotalOrderAmount(totalOrderAmount);
                    billDetail.setTotalPayAmount(totalPayAmount);
                    billDetail.setTotalRefundAmount(BigDecimal.ZERO);//TODO 目前没有退款功能，退款总金额默认0
                    billDetail.setBalAmount(BigDecimal.ZERO);
                    billDetail.setBalRefundTotalAmount(BigDecimal.ZERO);
                    billDetail.setWxAmount(BigDecimal.ZERO);
                    billDetail.setWxRefundAmount(BigDecimal.ZERO);
                    billDetail.setZfbAmount(BigDecimal.ZERO);
                    billDetail.setZfbRefundAmount(BigDecimal.ZERO);
                    billDetail.setVoucherAmount(BigDecimal.ZERO);
                    if (PayConstant.PayMethod.Balance.getCode().equals(payMethod)) {
                        billDetail.setBalAmount(totalPayAmount);
                    }
                    if (PayConstant.PayMethod.WXPay.getCode().equals(payMethod)) {
                        billDetail.setWxAmount(totalPayAmount);
                    }
                    if (PayConstant.PayMethod.ZFBPay.getCode().equals(payMethod)) {
                        billDetail.setZfbAmount(totalPayAmount);
                    }
                    if (PayConstant.PayMethod.Voucher.getCode().equals(payMethod)) {
                        billDetail.setVoucherAmount(totalPayAmount);
                    }
                    billDetail.setProductName(productMap.get(productId));
                    billDetail.setAccount(userMap.get(accountId));
                    billDetail.setBillingMethod(billingMethodMap.get(productId));
                    billDetail.setCreateDate(now);
                    billDetails.add(billDetail);
                }
            }
        }
        if (packOrders.size() > 0) {
            for (MallPackOrder packOrder : packOrders) {
                boolean billDetailFlag = false;
                boolean newBillDetailFlag = false;
                String siteCode = packOrder.getSiteCode();
                String accountId = packOrder.getAccountId();
                String productId = packOrder.getProductId();
                String payMethod = packOrder.getPayMethod();
                BigDecimal totalOrderAmount = packOrder.getOrderAmount();//订单总金额
                BigDecimal totalPayAmount = packOrder.getPayAmount();//支付总金额
                BigDecimal totalLimPri = packOrder.getLimPri();//限价总金额
                //判断已经处理的数据有没有相同的，有则将套餐包账单累加上去
                if (billDetails.size() > 0) {
                    for (BillDetail billDetail : billDetails) {
                        if (accountId.equals(billDetail.getAccountId()) &&
                                productId.equals(billDetail.getProductId())) {
                            billDetail.setTotalOrderAmount(billDetail.getTotalOrderAmount().add(totalOrderAmount));
                            billDetail.setTotalLmiPri(billDetail.getTotalLmiPri().add(totalLimPri));
                            billDetail.setTotalPayAmount(billDetail.getTotalPayAmount().add(totalPayAmount));
                            if (PayConstant.PayMethod.Balance.getCode().equals(payMethod)) {
                                billDetail.setBalAmount(billDetail.getBalAmount().add(totalPayAmount));
                            }
                            if (PayConstant.PayMethod.WXPay.getCode().equals(payMethod)) {
                                billDetail.setWxAmount(billDetail.getWxAmount().add(totalPayAmount));
                            }
                            if (PayConstant.PayMethod.ZFBPay.getCode().equals(payMethod)) {
                                billDetail.setZfbAmount(billDetail.getZfbAmount().add(totalPayAmount));
                            }
                            if (PayConstant.PayMethod.Voucher.getCode().equals(payMethod)) {
                                billDetail.setVoucherAmount(billDetail.getVoucherAmount().add(totalPayAmount));
                            }
                            billDetailFlag = true;
                            break;
                        }
                    }
                }
                if (newBillDetails.size() > 0) {
                    for (NewBillDetail newBillDetail : newBillDetails) {
                        if (accountId.equals(newBillDetail.getAccountId()) &&
                                productId.equals(newBillDetail.getProductId()) &&
                                payMethod.equals(newBillDetail.getPayMethod())) {
                            newBillDetail.setTotalLmiPri(newBillDetail.getTotalLmiPri().add(totalLimPri));
                            newBillDetail.setTotalPayAmount(newBillDetail.getTotalPayAmount().add(totalPayAmount));
                            newBillDetail.setTotalOrderAmount(newBillDetail.getTotalOrderAmount().add(totalOrderAmount));
                            newBillDetailFlag = true;
                            break;
                        }
                    }

                }
                if (!productMap.containsKey(productId)) {
                    //查询商品信息
                    JSONArray jsonArray = ps.queryProduct(token, productId);
                    if (null != jsonArray && jsonArray.size() == 1) {
                        String productName = jsonArray.getJSONObject(0).getString("name");
                        String billingMethod = jsonArray.getJSONObject(0).getString("saleChannel");
                        productMap.put(productId, productName);
                        billingMethodMap.put(productId, billingMethod);
                    }
                }
                if (!userMap.containsKey(accountId)) {
                    //查询用户名
                    UserDetail userDetail = us.userDetail(siteCode, accountId, token,"sobeyLingYunMall");
                    if (null != userDetail) {
                        userMap.put(accountId, userDetail.getLogin_name());
                    }
                }
                if (!billDetailFlag) {
                    BillDetail billDetail = new BillDetail();
                    billDetail.setUuid(IdWorker.get32UUID());
                    billDetail.setBillUuid(billUuid);
                    billDetail.setAccountId(accountId);
                    billDetail.setSiteCode(siteCode);
                    billDetail.setBillType(PayConstant.TransType.CONSUMPTION.getCode());
                    billDetail.setProductId(productId);
                    billDetail.setTotalLmiPri(totalLimPri);
                    billDetail.setTotalOrderAmount(totalOrderAmount);
                    billDetail.setTotalPayAmount(totalPayAmount);
                    billDetail.setTotalRefundAmount(BigDecimal.ZERO);//TODO 目前没有退款功能，退款总金额默认0
                    billDetail.setBalAmount(BigDecimal.ZERO);
                    billDetail.setBalRefundTotalAmount(BigDecimal.ZERO);
                    billDetail.setWxAmount(BigDecimal.ZERO);
                    billDetail.setWxRefundAmount(BigDecimal.ZERO);
                    billDetail.setZfbAmount(BigDecimal.ZERO);
                    billDetail.setZfbRefundAmount(BigDecimal.ZERO);
                    billDetail.setVoucherAmount(BigDecimal.ZERO);
                    if (PayConstant.PayMethod.Balance.getCode().equals(payMethod)) {
                        billDetail.setBalAmount(totalPayAmount);
                    }
                    if (PayConstant.PayMethod.WXPay.getCode().equals(payMethod)) {
                        billDetail.setWxAmount(totalPayAmount);
                    }
                    if (PayConstant.PayMethod.ZFBPay.getCode().equals(payMethod)) {
                        billDetail.setZfbAmount(totalPayAmount);
                    }
                    if (PayConstant.PayMethod.Voucher.getCode().equals(payMethod)) {
                        billDetail.setVoucherAmount(totalPayAmount);
                    }
                    billDetail.setBillingMethod(billingMethodMap.get(productId));
                    billDetail.setProductName(productMap.get(productId));
                    billDetail.setAccount(userMap.get(accountId));
                    billDetail.setCreateDate(now);
                    billDetails.add(billDetail);
                }
                if (!newBillDetailFlag) {
                    NewBillDetail newBillDetail = new NewBillDetail();
                    newBillDetail.setUuid(IdWorker.get32UUID());
                    newBillDetail.setAccountId(accountId);
                    newBillDetail.setBillType(PayConstant.TransType.CONSUMPTION.getCode());
                    newBillDetail.setProductId(productId);
                    newBillDetail.setSiteCode(siteCode);
                    newBillDetail.setPayMethod(payMethod);
                    newBillDetail.setBillDate(DateUtil.format(start, "yyyy/MM"));
                    newBillDetail.setTotalRefundAmount(BigDecimal.ZERO);
                    newBillDetail.setTotalOrderAmount(totalOrderAmount == null ? BigDecimal.ZERO : totalOrderAmount);
                    newBillDetail.setTotalPayAmount(totalPayAmount == null ? BigDecimal.ZERO : totalPayAmount);
                    newBillDetail.setTotalLmiPri(totalLimPri == null ? BigDecimal.ZERO : totalLimPri);
                    newBillDetail.setProductName(productMap.get(productId));
                    newBillDetail.setBillingMethod(billingMethodMap.get(productId));
                    newBillDetail.setAccount(userMap.get(accountId));
                    newBillDetail.setCreateDate(now);
                    newBillDetails.add(newBillDetail);
                }

            }
        }
        //统计个人账单表
        ArrayList<PersonalBill> personalBills = new ArrayList<>(32);
        //统计账单总表
        bill.setBillDate(DateUtil.format(start, "yyyy/MM"));
        bill.setCreateDate(now);
        //这里与金额相关的先提前设置0，防止后边出现空指针
        bill.setBalAmount(BigDecimal.ZERO);
        bill.setBalRefundTotalAmount(BigDecimal.ZERO);
        bill.setWxAmount(BigDecimal.ZERO);
        bill.setWxRefundAmount(BigDecimal.ZERO);
        bill.setZfbAmount(BigDecimal.ZERO);
        bill.setZfbRefundAmount(BigDecimal.ZERO);
        bill.setVoucherAmount(BigDecimal.ZERO);
        bill.setTotalAmount(BigDecimal.ZERO);
        bill.setRefundTotalAmount(BigDecimal.ZERO);
        bill.setLimPriAmount(BigDecimal.ZERO);
        if (billDetails.size() > 0) {
            boolean flag = false;
            for (BillDetail billDetail : billDetails) {
                bill.setBalAmount(bill.getBalAmount().add(billDetail.getBalAmount()));
                bill.setBalRefundTotalAmount(bill.getBalRefundTotalAmount().add(billDetail.getBalRefundTotalAmount()));
                bill.setWxAmount(bill.getWxAmount().add(billDetail.getWxAmount()));
                bill.setWxRefundAmount(bill.getWxRefundAmount().add(billDetail.getWxRefundAmount()));
                bill.setZfbAmount(bill.getZfbAmount().add(billDetail.getZfbAmount()));
                bill.setZfbRefundAmount(bill.getZfbRefundAmount().add(billDetail.getZfbRefundAmount()));
                bill.setVoucherAmount(bill.getVoucherAmount().add(billDetail.getVoucherAmount()));
                bill.setTotalAmount(bill.getTotalAmount().add(billDetail.getTotalPayAmount()));
                bill.setRefundTotalAmount(bill.getRefundTotalAmount().add(billDetail.getTotalRefundAmount()));
                bill.setLimPriAmount(bill.getLimPriAmount().add(billDetail.getTotalLmiPri()));
                if (personalBills.size() > 0) {
                    for (PersonalBill personalBill : personalBills) {
                        if (personalBill.getAccountId().equals(billDetail.getAccountId())) {
                            billDetail.setPersonalBillUuid(personalBill.getUuid());
                            personalBill.setBalAmount(personalBill.getBalAmount().add(billDetail.getBalAmount()));
                            personalBill.setBalRefundTotalAmount(personalBill.getBalRefundTotalAmount().add(billDetail.getBalRefundTotalAmount()));
                            personalBill.setWxAmount(personalBill.getWxAmount().add(billDetail.getWxAmount()));
                            personalBill.setWxRefundAmount(personalBill.getWxRefundAmount().add(billDetail.getWxRefundAmount()));
                            personalBill.setZfbAmount(personalBill.getZfbAmount().add(billDetail.getZfbAmount()));
                            personalBill.setZfbRefundAmount(personalBill.getZfbRefundAmount().add(billDetail.getZfbRefundAmount()));
                            personalBill.setVoucherAmount(personalBill.getVoucherAmount().add(billDetail.getVoucherAmount()));
                            personalBill.setTotalAmount(personalBill.getTotalAmount().add(billDetail.getTotalPayAmount()));
                            personalBill.setRefundTotalAmount(personalBill.getRefundTotalAmount().add(billDetail.getTotalRefundAmount()));
                            personalBill.setLimPriAmount(personalBill.getLimPriAmount().add(billDetail.getTotalLmiPri()));
                            flag = true;
                            break;
                        }
                    }
                }
                if (flag) {
                    flag = false;
                    continue;
                }
                PersonalBill personalBill = new PersonalBill();
                String perBillUuid = IdWorker.get32UUID();
                billDetail.setPersonalBillUuid(perBillUuid);
                personalBill.setUuid(perBillUuid);
                personalBill.setSiteCode(billDetail.getSiteCode());
                personalBill.setAccountId(billDetail.getAccountId());
                personalBill.setAccount(billDetail.getAccount());
                personalBill.setBalAmount(billDetail.getBalAmount());
                personalBill.setBalRefundTotalAmount(billDetail.getBalRefundTotalAmount());
                personalBill.setWxAmount(billDetail.getWxAmount());
                personalBill.setWxRefundAmount(billDetail.getWxRefundAmount());
                personalBill.setZfbAmount(billDetail.getZfbAmount());
                personalBill.setZfbRefundAmount(billDetail.getZfbRefundAmount());
                personalBill.setVoucherAmount(billDetail.getVoucherAmount());
                personalBill.setTotalAmount(billDetail.getTotalPayAmount());
                personalBill.setRefundTotalAmount(billDetail.getTotalRefundAmount());
                personalBill.setLimPriAmount(billDetail.getTotalLmiPri());
                personalBill.setBillDate(DateUtil.format(start, "yyyy/MM"));
                personalBill.setCreateDate(now);
                //查询用户对应的销售
                Map<String, Object> map = new HashMap<>();
                map.put("customerUserCode", billDetail.getAccountId());
                List<Salescustomer> salescustomers = salescustomerService.selectByMap(map);
                if (null != salescustomers && salescustomers.size() > 0) {
                    personalBill.setSaleAccountId(salescustomers.get(0).getSaleUserCode());
                }
                personalBills.add(personalBill);
            }
        }

        //保存数据
        BillService bs = SpringContextHolder.getBean(BillService.class);
        bs.save(bill, personalBills, billDetails, newBillDetails);
    }
}
