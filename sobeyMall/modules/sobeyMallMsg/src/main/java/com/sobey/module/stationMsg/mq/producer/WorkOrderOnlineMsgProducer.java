package com.sobey.module.stationMsg.mq.producer;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.module.stationMsg.entity.WorkOrderMsg;
import com.sobey.module.stationMsg.mq.constant.BindingName;
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
 * @createTime 2020/8/21 19:52
 */
@Component
public class WorkOrderOnlineMsgProducer implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    private static final Logger log = LoggerFactory.getLogger(MsgProducer.class);

    private RabbitTemplate rabbit;

    @Autowired
    public void WorkOrderOnlineMsgProducer(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
        rabbit.setConfirmCallback(this);
        rabbit.setReturnCallback(this);
    }

    public void pushMsg(WorkOrderMsg onlineMsg) {
        if (null == onlineMsg) {
            return;
        }
        CorrelationData correlation = new CorrelationData(IdWorker.getIdStr());
        String jsonMsg = JSON.toJSONString(onlineMsg);
        Message message = MessageBuilder.withBody(jsonMsg.getBytes(StandardCharsets.UTF_8)).build();
        rabbit.convertAndSend(BindingName.WORK_ORDER_ONLINE_MSG_EXCHANGE, BindingName.WORK_ORDER_ONLINE_MSG_ROUTING_KEY, message, correlation);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {

    }
}
