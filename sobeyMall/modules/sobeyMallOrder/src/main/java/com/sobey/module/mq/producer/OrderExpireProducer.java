package com.sobey.module.mq.producer;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.module.mq.constant.BindingConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author WCY
 * @createTime 2020/7/22 9:47
 */
@Component
public class OrderExpireProducer implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    private static final Logger log = LoggerFactory.getLogger(ChargeProducer.class);

    private RabbitTemplate rabbit;

    @Autowired
    public OrderExpireProducer(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
        rabbit.setConfirmCallback(this);
        rabbit.setReturnCallback(this);
    }

    public void produce(String orderNo){
        if (StringUtils.isBlank(orderNo)){
            return;
        }
        Message msg = MessageBuilder.withBody(orderNo.getBytes(StandardCharsets.UTF_8)).build();
        String msgId = IdWorker.getIdStr();
        CorrelationData correlationData = new CorrelationData(msgId);
        log.info("待支付订单{}过期倒计时消息推送到mq,消息Id:{}",orderNo,msgId);
        rabbit.convertAndSend(BindingConstant.READY_ORDER_EXPIRE_EXCHANGE,BindingConstant.READY_ORDER_EXPIRE_ROUTING_KEY,msg,correlationData);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack){
            log.info("消息Id为{}的待支付订单过期倒计时消息推送到交换器失败,原因:{}",correlationData.getId(),cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        String orderNo = new String(message.getBody(),StandardCharsets.UTF_8);
        log.info("待支付订单过期消息推送到队列失败,replyCode:{},desc:{},消息主体:{}",replyCode,replyText,orderNo);
    }
}
