package com.sobey.module.stationMsg.mq.producer;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.module.stationMsg.model.MallMsg;
import com.sobey.module.stationMsg.mq.constant.BindingName;
import com.sobey.module.stationMsg.service.MallMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @Author WCY
 * @CreateTime 2020/4/7 11:38
 * 消息生产者
 */
@Component
public class MsgProducer implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    private static final Logger log = LoggerFactory.getLogger(MsgProducer.class);

    private RabbitTemplate rabbit;
    @Autowired
    private MallMsgService mms;

    @Autowired
    public void MsgProducer(RabbitTemplate rabbit){
        this.rabbit = rabbit;
        rabbit.setConfirmCallback(this);
        rabbit.setReturnCallback(this);
    }

    public void creatMsg(@NotNull MallMsg mallMsg){

        if (null == mallMsg){
            return;
        }
        CorrelationData correlation = new CorrelationData(IdWorker.getIdStr());
        String jsonMsg = JSON.toJSONString(mallMsg);
        try {
            Message message = MessageBuilder.withBody(jsonMsg.getBytes(StandardCharsets.UTF_8)).build();
            rabbit.convertAndSend(BindingName.EXCHANGE_NAME,BindingName.ROUTING_KEY_NAME,message,correlation);
            mms.insert(mallMsg);
        } catch (Exception e) {
            log.info("消息推送异常",e);
            mms.insert(mallMsg);
        }

    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("消息推送成功,消息ID:" + correlationData.getId());
            return;
        }
        log.info("消息推送失败,原因:" + cause);
        byte[] bytes = correlationData.getReturnedMessage().getBody();
        String msg = null;
        try {
            msg = new String(bytes, "UTF-8");

            MallMsg mallMsg = JSON.parseObject(msg, MallMsg.class);
            mms.insert(mallMsg);
        } catch (UnsupportedEncodingException e) {
            log.info("消息转换异常:", e);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        try {
            String msg = new String(message.getBody(), "UTF-8");
            log.info("推送到队列失败,code:" + replyCode + ",desc:" + replyText + ",消息主体message:" + msg);
            MallMsg mallMsg = JSON.parseObject(msg, MallMsg.class);
            mms.insert(mallMsg);
        } catch (Exception e) {
            log.info("returnedMessage保存异常", e);
        }
    }
}
