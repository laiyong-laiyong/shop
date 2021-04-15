package com.sobey.module.productservice.destroy;

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
 * @createTime 2020/6/8 14:36
 * 推送销毁计时的服务到队列
 */
@Component
public class DestroyServicePublisher implements RabbitTemplate.ConfirmCallback {

    private static final Logger log = LoggerFactory.getLogger(DestroyServicePublisher.class);

    private RabbitTemplate rabbit;

    @Autowired
    public DestroyServicePublisher(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
        rabbit.setConfirmCallback(this);
    }

    /**
     * 服务主键
     * @param serviceUuid
     */
    public void publishMsg(String serviceUuid){

        if (StringUtils.isBlank(serviceUuid)){
            return;
        }
        Message msg = MessageBuilder.withBody(serviceUuid.getBytes(StandardCharsets.UTF_8)).build();
        rabbit.convertAndSend(BindingConstant.READY_DESTROY_EXCHANGE,BindingConstant.READY_DESTROY_ROUTING_KEY,msg,new CorrelationData(IdWorker.getIdStr()));
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, java.lang.String cause) {
        if (ack){
            log.info("冻结服务信息推送成功");
        }
        if (!ack){
            String msg = new String(correlationData.getReturnedMessage().getBody(), StandardCharsets.UTF_8);
            log.info("冻结服务信息推送失败,服务id:{},失败原因:{}",msg,cause);
        }
    }
}
