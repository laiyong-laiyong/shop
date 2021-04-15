package com.sobey.module.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/19 15:16
 */
@Configuration
public class RabbitConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Bean("rabbit")//设置为多例模式保证每个RabbitTemplate都会收到确认回调
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMandatory(true);
        template.setEncoding("UTF-8");
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public RabbitAdmin admin(){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

        //与服务销毁相关的队列配置
        rabbitAdmin.declareExchange(deadExchange());
        rabbitAdmin.declareExchange(readyVoucherExpireExchange());
        rabbitAdmin.declareQueue(readyVoucherExpireQueue());
        rabbitAdmin.declareQueue(voucherExpireQueue());
        rabbitAdmin.declareBinding(voucherExpireBinding());
        rabbitAdmin.declareBinding(readyVoucherExpireBinding());

        return rabbitAdmin;
    }

    /**
     * 代金券过期计时队列
     * @return
     */
    @Bean
    public Queue readyVoucherExpireQueue(){
        Map<String,Object> args = new HashMap<>();
        args.put("x-message-ttl",BindingConstant.EXPIRE_TIME);//消息存在的时长
        args.put("x-dead-letter-exchange",BindingConstant.DEAD_EXCHANGE_VOUCHER_EXPIRE);//死信交换器
        args.put("x-dead-letter-routing-key",BindingConstant.DEAD_ROUTING_KEY_VOUCHER_EXPIRE);//死信路由密钥
        return new Queue(BindingConstant.READY_VOUCHER_EXPIRE_QUEUE,true,false,false,args);
    }

    @Bean
    public DirectExchange readyVoucherExpireExchange(){
        return new DirectExchange(BindingConstant.READY_VOUCHER_EXPIRE_EXCHANGE,true,false);
    }

    @Bean
    public Binding readyVoucherExpireBinding(){
        return BindingBuilder.bind(readyVoucherExpireQueue()).to(readyVoucherExpireExchange()).with(BindingConstant.READY_VOUCHER_EXPIRE_ROUTING_KEY);
    }

    /**
     * 代金券过期队列
     * @return
     */
    @Bean
    public Queue voucherExpireQueue(){
        return new Queue(BindingConstant.VOUCHER_EXPIRE_QUEUE,true);
    }

    @Bean
    public DirectExchange deadExchange(){
        return new DirectExchange(BindingConstant.DEAD_EXCHANGE_VOUCHER_EXPIRE,true,false);
    }

    @Bean
    public Binding voucherExpireBinding(){
        return BindingBuilder.bind(voucherExpireQueue()).to(deadExchange()).with(BindingConstant.DEAD_ROUTING_KEY_VOUCHER_EXPIRE);
    }



}
