package com.sobey.module.productservice.destroy;

import com.rabbitmq.client.Channel;
import com.sobey.module.order.ServiceStatus;
import com.sobey.module.productservice.model.ServiceInfo;
import com.sobey.module.mq.constant.BindingConstant;
import com.sobey.module.productservice.service.ServiceInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author WCY
 * @createTime 2020/6/8 14:36
 * 服务销毁监听
 */
@Component
public class DestroyServiceListener {

    private static final Logger log = LoggerFactory.getLogger(DestroyServiceListener.class);

    @Autowired
    private ServiceInfoService sis;

    @RabbitHandler
    @RabbitListener(bindings =
    @QueueBinding(value = @Queue(name = BindingConstant.DESTROY_QUEUE),
            exchange = @Exchange(name = BindingConstant.DEAD_EXCHANGE_DESTROY),
            key = BindingConstant.DEAD_ROUTING_KEY_DESTROY), ackMode = "AUTO")
    public void destroyService(Message msg, Channel channel) {
        if (null == msg) {
            return;
        }
        String uuid = new String(msg.getBody(), StandardCharsets.UTF_8);
        ServiceInfo serviceInfo = sis.selectById(uuid);
        if (null == serviceInfo) {
            log.info("未查询到uuid为{}的服务", uuid);
            return;
        }
        //状态不是正常和开通中一律销毁
        if (!ServiceStatus.Normal.getCode().equals(serviceInfo.getServiceStatus()) &&
                !ServiceStatus.Opening.getCode().equals(serviceInfo.getServiceStatus())) {
            ServiceInfo update = new ServiceInfo();
            //TODO 调用销毁接口
            update.setUuid(uuid);
            update.setServiceStatus(ServiceStatus.Destroy.getCode());
            sis.updateById(update);
            log.info("销毁服务(uuid:{})成功", uuid);
        }
    }

}
