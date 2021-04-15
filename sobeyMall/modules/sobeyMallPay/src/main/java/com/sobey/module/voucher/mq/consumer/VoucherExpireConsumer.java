package com.sobey.module.voucher.mq.consumer;

import com.sobey.module.config.rabbitmq.BindingConstant;
import com.sobey.module.voucher.enumeration.ReceiveStatus;
import com.sobey.module.voucher.model.Voucher;
import com.sobey.module.voucher.service.VoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author WCY
 * @createTime 2020/7/13 17:47
 */
@Component
public class VoucherExpireConsumer {

    private static final Logger log = LoggerFactory.getLogger(VoucherExpireConsumer.class);

    @Autowired
    private VoucherService vs;

    @RabbitHandler
    @RabbitListener(bindings =
    @QueueBinding(value = @Queue(value = BindingConstant.VOUCHER_EXPIRE_QUEUE, durable = "true"),
            exchange = @Exchange(value = BindingConstant.DEAD_EXCHANGE_VOUCHER_EXPIRE),
            key = BindingConstant.DEAD_ROUTING_KEY_VOUCHER_EXPIRE), ackMode = "AUTO")
    public void receive(Message message){
        String uuid = new String(message.getBody());
        Voucher voucher = vs.selectById(uuid);
        if (null == voucher){
            log.info("uuid为{}的代金券不存在",uuid);
            return;
        }
        if (ReceiveStatus.No.getCode().equals(voucher.getReceiveStatus())){
            voucher.setReceiveStatus(ReceiveStatus.Expire.getCode());
            voucher.setUpdateTime(new Date());
            vs.updateById(voucher);
        }
    }

}
