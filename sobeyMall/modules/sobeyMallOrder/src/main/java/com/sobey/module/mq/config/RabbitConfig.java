package com.sobey.module.mq.config;

import com.sobey.module.mq.constant.BindingConstant;
import org.springframework.amqp.core.*;
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
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMandatory(true);
        template.setEncoding("UTF-8");
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

//    @Bean
//    public DirectExchange chargeExchange(){
//        return new DirectExchange(BindingConstant.CHARGE_DIRECT_EXCHANGE,true,false);
//    }
//
//    @Bean
//    public Queue chargeQueue(){
//        return new Queue(BindingConstant.CHARGE_QUEUE,true);
//    }
//
//    @Bean
//    public Binding chargeBinding(){
//        return BindingBuilder.bind(chargeQueue()).to(chargeExchange()).with(BindingConstant.ROUTING_KEY);
//    }

    @Bean
    public RabbitAdmin admin(){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        //与计费相关的队列配置
//        rabbitAdmin.declareExchange(chargeExchange());
//        rabbitAdmin.declareQueue(chargeQueue());
//        rabbitAdmin.declareBinding(chargeBinding());

        //与服务销毁相关的队列配置
        rabbitAdmin.declareExchange(readyDestroyExchange());
        rabbitAdmin.declareQueue(readyDestroyQueue());
        rabbitAdmin.declareBinding(readyDestroyBinding());
//        rabbitAdmin.declareQueue(destroyServiceQueue());
//        rabbitAdmin.declareExchange(deadExchange());
//        rabbitAdmin.declareBinding(destroyServiceBinding());

        //待支付订单倒计时相关
        rabbitAdmin.declareExchange(readyOrderExpireExchange());
        rabbitAdmin.declareQueue(readyOrderExpireQueue());
        rabbitAdmin.declareBinding(readOrderExpireBinding());
//        rabbitAdmin.declareQueue(orderExpireQueue());
//        rabbitAdmin.declareExchange(deadOrderExpireExchange());
//        rabbitAdmin.declareBinding(deadOrderExpireBinding());

        return rabbitAdmin;
    }

    /**
     * 冻结期服务队列
     * @return
     */
    @Bean
    public Queue readyDestroyQueue(){
        Map<String,Object> args = new HashMap<>();
        args.put("x-message-ttl",BindingConstant.FREEZING_TIME);//消息存在的时长
        args.put("x-dead-letter-exchange",BindingConstant.DEAD_EXCHANGE_DESTROY);//死信交换器
        args.put("x-dead-letter-routing-key",BindingConstant.DEAD_ROUTING_KEY_DESTROY);//死信路由密钥
        return new Queue(BindingConstant.READY_DESTROY_QUEUE,true,false,false,args);
    }

    @Bean
    public DirectExchange readyDestroyExchange(){
        return new DirectExchange(BindingConstant.READY_DESTROY_EXCHANGE,true,false);
    }

    @Bean
    public Binding readyDestroyBinding(){
        return BindingBuilder.bind(readyDestroyQueue()).to(readyDestroyExchange()).with(BindingConstant.READY_DESTROY_ROUTING_KEY);
    }

    /**
     * 服务销毁队列
     * @return
     */
    @Bean
    public Queue destroyServiceQueue(){
        return new Queue(BindingConstant.DESTROY_QUEUE,true);
    }

    @Bean
    public DirectExchange deadExchange(){
        return new DirectExchange(BindingConstant.DEAD_EXCHANGE_DESTROY,true,false);
    }

    @Bean
    public Binding destroyServiceBinding(){
        return BindingBuilder.bind(destroyServiceQueue()).to(deadExchange()).with(BindingConstant.DEAD_ROUTING_KEY_DESTROY);
    }

    /**
     * 待支付订单过期倒计时队列
     * @return
     */
    @Bean
    public Queue readyOrderExpireQueue(){
        Map<String,Object> args = new HashMap<>();
        args.put("x-message-ttl",BindingConstant.ORDER_EXPIRE_TIME);//消息存在的时长
        args.put("x-dead-letter-exchange",BindingConstant.DEAD_ORDER_EXPIRE_EXCHANGE);//死信交换器
        args.put("x-dead-letter-routing-key",BindingConstant.DEAD_ORDER_EXPIRE_ROUTING_KEY);//死信路由密钥
        return new Queue(BindingConstant.READY_ORDER_EXPIRE_QUEUE,true,false,false,args);
    }

    @Bean
    public DirectExchange readyOrderExpireExchange(){
        return new DirectExchange(BindingConstant.READY_ORDER_EXPIRE_EXCHANGE,true,false);
    }

    @Bean
    public Binding readOrderExpireBinding(){
        return BindingBuilder.bind(readyOrderExpireQueue()).to(readyOrderExpireExchange()).with(BindingConstant.READY_ORDER_EXPIRE_ROUTING_KEY);
    }

    /**
     * 待支付订单过期队列
     * @return
     */
    @Bean
    public Queue orderExpireQueue(){
        return new Queue(BindingConstant.ORDER_EXPIRE_QUEUE,true,false,false);
    }

    @Bean
    public DirectExchange deadOrderExpireExchange(){
        return new DirectExchange(BindingConstant.DEAD_ORDER_EXPIRE_EXCHANGE,true,false);
    }

    @Bean
    public Binding deadOrderExpireBinding(){
        return BindingBuilder.bind(orderExpireQueue()).to(deadOrderExpireExchange()).with(BindingConstant.DEAD_ORDER_EXPIRE_ROUTING_KEY);
    }

}
