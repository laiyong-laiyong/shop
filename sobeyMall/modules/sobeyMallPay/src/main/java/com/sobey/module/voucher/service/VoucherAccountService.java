package com.sobey.module.voucher.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.module.common.enumeration.EffectiveDurationUnitType;
import com.sobey.module.common.enumeration.IsEffective;
import com.sobey.module.common.response.ResultInfo;
import com.sobey.module.common.util.DateUtil;
import com.sobey.module.common.util.MsgUtil;
import com.sobey.module.common.util.NoticePackUtil;
import com.sobey.module.fegin.mallPack.pack.entity.MallPack;
import com.sobey.module.fegin.mallPack.packOrder.entity.request.MallPackOrder;
import com.sobey.module.fegin.mallPack.packOrder.entity.request.MallPackResource;
import com.sobey.module.fegin.msg.enumeration.MsgSubType;
import com.sobey.module.fegin.order.entity.response.Order;
import com.sobey.module.fegin.serviceInfo.entity.response.ServiceInfo;
import com.sobey.module.fegin.serviceInfo.service.ServiceInfoService;
import com.sobey.module.order.OrderType;
import com.sobey.module.order.ServiceStatus;
import com.sobey.module.pay.PayConstant;
import com.sobey.module.pay.PayStatus;
import com.sobey.module.voucher.mapper.VoucherAccountMapper;
import com.sobey.module.voucher.model.VoucherAccount;
import com.sobey.util.business.header.HeaderUtil;
import com.sobey.util.common.appid.AppIdUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VoucherAccountService extends ServiceImpl<VoucherAccountMapper, VoucherAccount> {

    @Autowired
    private AppIdUtil appIdUtil;
    @Autowired
    private ServiceInfoService sis;
    @Autowired
    private VoucherAccountMapper vam;
    @Autowired
    private VoucherTransactionService vts;
    @Autowired
    private MsgUtil msgUtil;
    @Autowired
    private NoticePackUtil noticePackUtil;

    public VoucherAccount findByAccountId(String accountId) {
        return vam.findByAccountId(accountId);
    }

    public ResultInfo pay(Order order, VoucherAccount va) {
        ResultInfo rs = new ResultInfo();
        BigDecimal orderAmount = order.getOrderAmount();//代金券支付不享受折扣
        String orderType = order.getOrderType();
        String siteCode = order.getSiteCode();
        String accountId = order.getAccountId();
        String productId = order.getProductId();
        String versionId = order.getVersionId();
        BigDecimal vouAmount = va.getVouAmount();

        vouAmount = vouAmount.subtract(orderAmount);
        Date date = new Date();
        String token = HeaderUtil.getAuth();
        //服务信息
        ServiceInfo service = null;

        //修改订单状态并增加商品服务信息
        Map<String, Object> updateOrder = new HashMap<>();
        BigDecimal payAmount = order.getOrderAmount();
        updateOrder.put("payAmount", payAmount);
        updateOrder.put("payDate", DateUtil.format(date, DateUtil.FORMAT_1));
        updateOrder.put("paymentStatus", PayStatus.Paid.getCode());
        updateOrder.put("payMethod", PayConstant.PayMethod.Voucher.getCode());
        updateOrder.put("discount", 1);
        updateOrder.put("discountPrice", payAmount);

        //新购
        if (OrderType.New.getCode().equals(order.getOrderType())) {
            String appId = appIdUtil.getAppIdMD5(accountId, productId, versionId);
            //创建服务
            service = new ServiceInfo();
            //服务号
            String serviceNo = IdWorker.getIdStr();
            service.setUuid(IdWorker.get32UUID())
                    .setAppId(appId)
                    .setServiceNo(serviceNo)
                    .setAccountId(accountId)
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
                    .setVersionId(versionId)
                    .setSpecifications(order.getSpecifications())
                    .setSiteCode(siteCode)
                    .setRelatedProductId(order.getRelatedProductId())
                    .setRelatedProductName(order.getRelatedProductName());
            updateOrder.put("serviceNo", serviceNo);//更新订单中的服务编号
        }
        //续费商品
        if (OrderType.Renew.getCode().equals(order.getOrderType())) {
            //查询服务信息
            service = sis.select(token, order.getServiceNo());
            if (null == service) {
                rs.setRt_code("FAIL");
                rs.setRt_msg("未能查询到服务号为" + order.getServiceNo() + "的服务信息");
                return rs;
            }
            //续费增加时长
            Date expireDate = service.getExpireDate();
            if (ServiceStatus.Closed.getCode().equals(service.getServiceStatus())) {
                expireDate = date;
            }
            //单位天，用天来计算过期时间
            Date renewLater = DateUtils.addDays(expireDate, order.getDuration());
            service.setExpireDate(renewLater);
            service.setUpdateDate(date);
            service.setServiceStatus(ServiceStatus.Normal.getCode());
        }
        va.setVouAmount(vouAmount.setScale(2, RoundingMode.DOWN));
        va.setUpdateTime(date);
        vts.update(token, order, updateOrder, va, service);
        //发送消息,这里只发送续费消息，如果是新购会在创建完服务后发送消息
        if (OrderType.Renew.getCode().equals(orderType)) {
            msgUtil.sendMsg(token, MsgSubType.ServiceRenewNotice.getCode(), siteCode, accountId, productId, service.getServiceNo(), payAmount.toString(), "", "");
        }

        rs.setRt_code("SUCCESS");
        rs.setRt_msg("支付成功");
        return rs;
    }


    /**
     * 支付套餐包订单
     *
     * @param order
     * @param va
     * @return
     */
    public ResultInfo payPackOrder(MallPackOrder order, VoucherAccount va) {

        ResultInfo ri = new ResultInfo();
        Date date = new Date();
        BigDecimal orderAmount = order.getOrderAmount();
        String token = HeaderUtil.getAuth();
        BigDecimal vouAmount = va.getVouAmount();
        vouAmount = vouAmount.subtract(orderAmount);

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
        //更新信息
        va.setVouAmount(vouAmount);
        Map<String, Object> updateOrder = new HashMap<>();
        updateOrder.put("payAmount", order.getOrderAmount());
        updateOrder.put("discount", 1);
        updateOrder.put("discountPrice", order.getOrderAmount());
        updateOrder.put("payStatus", PayStatus.Paid.getCode());
        updateOrder.put("payDate", DateUtil.format(date, DateUtil.FORMAT_1));
        updateOrder.put("payMethod", PayConstant.PayMethod.Voucher.getCode());
        vts.updatePackData(token, order.getId(), updateOrder, va, mallPack);
        //发送套餐包购买通知
        noticePackUtil.noticePack(token, order.getPackUuid(), order.getPackName(), order.getProductId(), order.getAccountId(), order.getSiteCode(), expireDate, resources);
        ri.setRt_code("SUCCESS");
        ri.setRt_msg("购买成功");
        return ri;
    }

}
