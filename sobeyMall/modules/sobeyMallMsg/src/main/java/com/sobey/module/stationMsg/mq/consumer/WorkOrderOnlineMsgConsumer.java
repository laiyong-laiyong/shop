package com.sobey.module.stationMsg.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.sobey.module.stationMsg.entity.WorkOrderMsg;
import com.sobey.module.stationMsg.mq.constant.BindingName;
import com.sobey.module.stationMsg.socketIo.SocketIoEventHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author WCY
 * @createTime 2020/8/21 19:52
 */
@Component
public class WorkOrderOnlineMsgConsumer {

    private static final Logger log = LoggerFactory.getLogger(MsgConsumer.class);

    @Autowired
    private SocketIoEventHandler msgHandler;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "", durable = "true",exclusive = "true",autoDelete = "false"),
            exchange = @Exchange(value = BindingName.WORK_ORDER_ONLINE_MSG_EXCHANGE,type = ExchangeTypes.TOPIC),
            key = BindingName.WORK_ORDER_ONLINE_MSG_ROUTING_KEY), ackMode = "AUTO")
    @RabbitHandler
    public void receive(Message msg){

        if (null == msg) {
            log.info("收到工单聊天队列的消息为null");
            return;
        }
        try {
            String message = new String(msg.getBody(), StandardCharsets.UTF_8);
            if (StringUtils.isBlank(message)) {
                log.info("消息体为空");
                return;
            }
            log.info("接收到工单聊天队列的消息:"+message);
            WorkOrderMsg orderMsg = JSON.parseObject(message, WorkOrderMsg.class);
            msgHandler.onlineChat(orderMsg);
        } catch (Exception e) {
            log.info("工单在线聊天消息发送异常",e);
        }
    }

}
