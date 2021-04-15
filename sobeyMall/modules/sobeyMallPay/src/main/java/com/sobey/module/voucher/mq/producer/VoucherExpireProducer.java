package com.sobey.module.voucher.mq.producer;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.module.config.rabbitmq.BindingConstant;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;

/**
 * @author WCY
 * @createTime 2020/7/13 17:21
 */
@Component
public class VoucherExpireProducer implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {

    private static final Logger log = LoggerFactory.getLogger(VoucherExpireProducer.class);

    private RabbitTemplate rabbit;

    @Autowired
    public VoucherExpireProducer(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
        rabbit.setConfirmCallback(this);
        rabbit.setReturnCallback(this);
    }

    /**
     * 推送代金券过期倒计时消息
     * @param uuids 代金券主键集合
     */
    public void produce(List<String> uuids){

        if (null == uuids || uuids.size() == 0){
            return;
        }
        for (String uuid : uuids) {
            if (StringUtils.isBlank(uuid)){
                continue;
            }
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentEncoding("UTF-8");

            Message message = MessageBuilder.withBody(uuid.getBytes(StandardCharsets.UTF_8)).andProperties(messageProperties).build();

            CorrelationData correlationData = new CorrelationData(IdWorker.getIdStr());
            rabbit.convertAndSend(BindingConstant.READY_VOUCHER_EXPIRE_EXCHANGE,BindingConstant.READY_VOUCHER_EXPIRE_ROUTING_KEY,message,correlationData);
        }

    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack){
            log.info("代金券过期消息推送失败:"+cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("代金券过期消息推送到队列失败,code:" + replyCode + ",desc:" + replyText + ",消息主体message:" + msg);
    }
}
