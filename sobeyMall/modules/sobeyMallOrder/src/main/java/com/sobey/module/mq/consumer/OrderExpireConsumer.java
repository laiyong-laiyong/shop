package com.sobey.module.mq.consumer;

import com.sobey.module.mallPack.model.MallPackOrder;
import com.sobey.module.mallPack.service.MallPackOrderService;
import com.sobey.module.mq.constant.BindingConstant;
import com.sobey.module.mq.constant.OrderExpireConstant;
import com.sobey.module.order.model.Order;
import com.sobey.module.order.service.OrderService;
import com.sobey.module.pay.PayStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WCY
 * @createTime 2020/7/22 11:21
 */
@Component
public class OrderExpireConsumer {

    private static final Logger log = LoggerFactory.getLogger(ChargeConsumer.class);

    @Autowired
    private MallPackOrderService mpos;
    @Autowired
    private OrderService os;

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = BindingConstant.ORDER_EXPIRE_QUEUE),
            exchange = @Exchange(name = BindingConstant.DEAD_ORDER_EXPIRE_EXCHANGE),
            key = BindingConstant.DEAD_ORDER_EXPIRE_ROUTING_KEY),ackMode = "AUTO")
    public void receive(Message msg) {

        if (null == msg){
            return;
        }
        try {
            String orderNo = new String(msg.getBody(), StandardCharsets.UTF_8);
            //套餐包订单
            if (orderNo.startsWith(OrderExpireConstant.PACK_ORDER_NO)){
                orderNo = orderNo.replace(OrderExpireConstant.PACK_ORDER_NO,"");
                MallPackOrder packOrder = mpos.findByOrderNo(orderNo);
                if (null != packOrder){
                    if (PayStatus.ToBePaid.getCode().equals(packOrder.getPayStatus())){
                        Map<String,Object> update = new HashMap<>();
                        update.put("payStatus",PayStatus.Expired.getCode());
                        mpos.update(packOrder.getId(),update);
                        return;
                    }
                }
            }
            //商品订单
            if (orderNo.startsWith(OrderExpireConstant.PRODUCT_ORDER_NO)){
                orderNo = orderNo.replace(OrderExpireConstant.PRODUCT_ORDER_NO,"");
                List<Order> orders = os.findByOrderNo(orderNo);
                if (null != orders && orders.size() == 1){
                    Order order = orders.get(0);
                    if (PayStatus.ToBePaid.getCode().equals(order.getPaymentStatus())){
                        Map<String,Object> update = new HashMap<>();
                        update.put("paymentStatus",PayStatus.Expired.getCode());
                        os.update(order.getId(),update);
                    }
                }
            }
        } catch (Exception e) {
            log.info("待支付订单过期监听队列异常:",e);
        }
    }

}
