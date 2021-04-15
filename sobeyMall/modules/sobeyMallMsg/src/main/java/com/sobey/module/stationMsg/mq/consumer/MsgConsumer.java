package com.sobey.module.stationMsg.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.sobey.module.stationMsg.model.MallMsg;
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
 * @Author WCY
 * @CreateTime 2020/4/3 18:27
 */
@Component
public class MsgConsumer{

    private static final Logger log = LoggerFactory.getLogger(MsgConsumer.class);

    @Autowired
    private SocketIoEventHandler socketIoEventHandler;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "", durable = "true",exclusive = "true",autoDelete = "false"),
            exchange = @Exchange(value = BindingName.EXCHANGE_NAME,type = ExchangeTypes.TOPIC),
            key = BindingName.ROUTING_KEY_NAME), ackMode = "AUTO")
    @RabbitHandler
    public void receiveMsg(Message msg) {
        if (null == msg) {
            log.info("收到队列的消息为null");
            return;
        }
        MallMsg mallMsg = null;
        try {
            String message = new String(msg.getBody(), StandardCharsets.UTF_8);
            if (StringUtils.isBlank(message)) {
                log.info("消息体为空");
                return;
            }
            log.info("接收到mq消息:"+message);
            mallMsg = JSON.parseObject(message, MallMsg.class);
            socketIoEventHandler.sendMsgToPerson(mallMsg);
        } catch (Exception e) {
            log.info("消息发送异常",e);
        }
    }
}
