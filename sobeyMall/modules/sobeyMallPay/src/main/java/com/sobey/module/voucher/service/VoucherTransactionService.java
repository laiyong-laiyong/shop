package com.sobey.module.voucher.service;

import com.sobey.module.fegin.mallPack.pack.entity.MallPack;
import com.sobey.module.fegin.mallPack.pack.service.MallPackService;
import com.sobey.module.fegin.mallPack.packOrder.service.PackOrderService;
import com.sobey.module.fegin.order.entity.response.Order;
import com.sobey.module.fegin.order.service.OrderService;
import com.sobey.module.fegin.serviceInfo.entity.response.ServiceInfo;
import com.sobey.module.fegin.serviceInfo.service.ServiceInfoService;
import com.sobey.module.order.OrderType;
import com.sobey.module.pay.PayStatus;
import com.sobey.module.voucher.mapper.VoucherAccountMapper;
import com.sobey.module.voucher.model.VoucherAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WCY
 * @createTime 2020/8/26 14:11
 */
@Component
public class VoucherTransactionService {

    @Autowired
    private OrderService os;
    @Autowired
    private VoucherAccountMapper vam;
    @Autowired
    private ServiceInfoService sis;
    @Autowired
    private MallPackService mps;
    @Autowired
    private PackOrderService pos;

    /**
     * 更新数据
     *
     * @param token
     * @param order
     * @param updateOrder
     * @param va
     * @param service
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(String token, Order order, Map<String, Object> updateOrder, VoucherAccount va, ServiceInfo service) {
        String orderType = order.getOrderType();
        os.update(token, order.getId(), updateOrder);
        vam.updateById(va);
        if (OrderType.New.getCode().equals(orderType)) {
            com.sobey.module.fegin.serviceInfo.entity.response.ResultInfo result = sis.createService(token, service);
            if (!"success".equalsIgnoreCase(result.getCode())) {
                //将订单支付状态改为支付失败
                Map<String, Object> map = new HashMap<>();
                map.put("paymentStatus", PayStatus.Failure.getCode());
                map.put("payAmount", null);
                map.put("payDate", null);
                map.put("payMethod", "");
                os.update(token, order.getId(), map);
                throw new RuntimeException("商品开通失败,支付失败");
            }
        }
        if (OrderType.Renew.getCode().equals(orderType)) {
            com.sobey.module.fegin.serviceInfo.entity.response.ResultInfo result = sis.renew(token, service);
            if (!"success".equalsIgnoreCase(result.getCode())) {
                //将订单支付状态改为支付失败
                Map<String, Object> map = new HashMap<>();
                map.put("paymentStatus", PayStatus.Failure.getCode());
                map.put("payAmount", null);
                map.put("payDate", null);
                map.put("payMethod", "");
                os.update(token, order.getId(), map);
                throw new RuntimeException("商品续费失败,支付失败");
            }
        }
    }
    /**
     * 更新数据
     *
     * @param token
     * @param orderId
     * @param updateOrder
     * @param va
     * @param mallPack
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePackData(String token, String orderId, Map<String, Object> updateOrder, VoucherAccount va, MallPack mallPack) {
        pos.update(token, orderId, updateOrder);
        vam.updateById(va);
        mps.createPack(token, mallPack);
    }

}
