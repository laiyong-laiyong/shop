package com.sobey.module.balance.service;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.balance.entity.RechargeBalanceParam;
import com.sobey.module.balance.mapper.BalanceMapper;
import com.sobey.module.balance.mapper.RechargeBalanceRecordsMapper;
import com.sobey.module.balance.model.Balance;
import com.sobey.module.balance.model.RechargeBalanceRecords;
import com.sobey.module.fegin.mallPack.pack.entity.MallPack;
import com.sobey.module.fegin.mallPack.pack.service.MallPackService;
import com.sobey.module.fegin.mallPack.packOrder.service.PackOrderService;
import com.sobey.module.fegin.order.entity.response.Order;
import com.sobey.module.fegin.order.service.OrderService;
import com.sobey.module.fegin.serviceInfo.entity.response.ResultInfo;
import com.sobey.module.fegin.serviceInfo.entity.response.ServiceInfo;
import com.sobey.module.fegin.serviceInfo.service.ServiceInfoService;
import com.sobey.module.order.OrderType;
import com.sobey.module.pay.PayConstant;
import com.sobey.module.pay.PayStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WCY
 * @createTime 2020/8/26 14:14
 */
@Component
public class BalanceTransactionService {

    @Autowired
    private MallPackService mps;
    @Autowired
    private PackOrderService pos;
    @Autowired
    private BalanceMapper bm;
    @Autowired
    private OrderService os;
    @Autowired
    private ServiceInfoService sis;
    @Autowired
    private RechargeBalanceRecordsMapper rbrm;

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
                               MallPack mallPack,
                               Balance balance) {
        pos.update(token, orderId, update);
        bm.updateById(balance);
        mps.createPack(token, mallPack);
    }

    /**
     * 更新数据
     *
     * @param order
     * @param orderInfo
     * @param balance
     * @param service
     */
    @Transactional
    public void updateData(String token, Order order, Map<String, Object> orderInfo, Balance balance, ServiceInfo service) {
        bm.updateById(balance);
        String id = order.getId();
        String orderType = order.getOrderType();
        os.update(token, id, orderInfo);
        if (PayConstant.TransType.CONSUMPTION.getCode().equals(order.getTransType())) {
            if (OrderType.New.getCode().equals(orderType)) {
                ResultInfo result = sis.createService(token, service);
                if (!"SUCCESS".equalsIgnoreCase(result.getCode())) {
                    //将订单支付状态改为支付失败
                    Map<String, Object> map = new HashMap<>();
                    map.put("paymentStatus", PayStatus.Failure.getCode());
                    map.put("payAmount", null);
                    map.put("payDate", null);
                    map.put("payMethod", "");
                    os.update(token, id, map);
                    throw new AppException(ExceptionType.ORDER_CODE_OPEN_FAIL, "商品开通失败", new RuntimeException());
                }
            }
            if (OrderType.Renew.getCode().equals(orderType)) {
                ResultInfo renew = sis.renew(token, service);
                if (!"SUCCESS".equalsIgnoreCase(renew.getCode())) {
                    //将订单支付状态改为支付失败
                    Map<String, Object> map = new HashMap<>();
                    map.put("paymentStatus", PayStatus.Failure.getCode());
                    map.put("payAmount", null);
                    map.put("payDate", null);
                    map.put("payMethod", "");
                    os.update(token, id, map);
                    throw new AppException(ExceptionType.ORDER_CODE_RENEW_FAIL, "商品续费失败", new RuntimeException());
                }
            }
        }
    }

    /**
     * 充值余额后更新数据并创建充值记录
     *
     * @param balAccount
     * @param param
     */
    @Transactional
    public void updateBalAccount(Balance balAccount, RechargeBalanceParam param) {
        Date now = new Date();
        balAccount.setUpdateDate(now);
        bm.updateById(balAccount);
        //创建充值记录
        RechargeBalanceRecords records = new RechargeBalanceRecords();
        BeanUtils.copyProperties(param, records);
        records.setUuid(IdWorker.get32UUID());
        records.setRechargeTime(now);
        rbrm.insert(records);
    }
}
