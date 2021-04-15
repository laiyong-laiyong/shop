package com.sobey.module.mq.producer;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.exception.ExceptionKit;
import com.sobey.module.mq.constant.BindingConstant;
import com.sobey.module.productservice.entity.charge.Charging;
import com.sobey.module.productservice.model.PublishFailMsg;
import com.sobey.module.productservice.service.PublishFailMsgService;
import com.sobey.module.utils.CacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/18 14:31
 */
@Component
public class ChargeProducer implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    private static final Logger log = LoggerFactory.getLogger(ChargeProducer.class);

    private RabbitTemplate rabbit;
    @Autowired
    private PublishFailMsgService pfms;

    @Autowired
    public ChargeProducer(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
        rabbit.setConfirmCallback(this);
        rabbit.setReturnCallback(this);
    }

    /**
     * 推送计费信息
     *
     * @param charging
     */
    public void publishChargeMsg(Charging charging) {
        if (null == charging) {
            return;
        }
        CorrelationData correlation = new CorrelationData(IdWorker.getIdStr());
        try {
            String token = CacheUtil.getToken();
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentEncoding("UTF-8");
            messageProperties.setHeader("token", token);
            byte[] bytes = JSON.toJSONString(charging).getBytes(StandardCharsets.UTF_8);
            Message message = MessageBuilder.withBody(bytes).andProperties(messageProperties).build();
            rabbit.convertAndSend(BindingConstant.CHARGE_DIRECT_EXCHANGE, BindingConstant.ROUTING_KEY, message, correlation);
        } catch (Exception e) {
            log.info("计费信息推送异常", e);
            //保存消息
           saveFailMsg(charging,"计费信息推送异常:"+ ExceptionKit.toString(e));
        }

    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("消息推送成功,消息ID:" + correlationData.getId());
            return;
        }
        log.info("计费信息推送失败,原因:" + cause);
        byte[] bytes = correlationData.getReturnedMessage().getBody();
        String msg = null;
        try {
            msg = new String(bytes, StandardCharsets.UTF_8);

            Charging charging = JSON.parseObject(msg, Charging.class);
            saveFailMsg(charging,"推送到队列失败,cause:" + cause);
        } catch (Exception e) {
            log.info("消息转换异常:", e);
        }

    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        try {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            log.info("推送到队列失败,code:" + replyCode + ",desc:" + replyText + ",消息主体message:" + msg);
            Charging charging = JSON.parseObject(msg, Charging.class);
            saveFailMsg(charging, "推送到队列失败,code:" + replyCode + ",desc:" + replyText);
        } catch (Exception e) {
            log.info("returnedMessage保存异常", e);
        }
    }

    public void saveFailMsg(Charging charging, String failReason) {
        PublishFailMsg failMsg = new PublishFailMsg();
        failMsg.setAppId(charging.getAppId());
        failMsg.setUserCode(charging.getUserCode());
        failMsg.setUuid(IdWorker.getIdStr());
        failMsg.setUsage(JSON.toJSONString(charging.getUsage()));
        failMsg.setFailReason(failReason);
        failMsg.setCreateDate(new Date());
        failMsg.setManualProcessStatus("0");
        pfms.insert(failMsg);
    }
}
