package com.sobey.module.config.rabbitmq;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootConfiguration
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
//    public RabbitAdmin rabbitAdmin(){
//        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
//        rabbitAdmin.declareQueue(mallMsgQueue());
//        rabbitAdmin.declareExchange(mallMsgExchange());
//        rabbitAdmin.declareBinding(mallMsgBinding());
//
//        return rabbitAdmin;
//    }
//
//    @Bean
//    public Queue mallMsgQueue(){
//        return new Queue("",true,true,false);
//    }
//
//    @Bean
//    public TopicExchange mallMsgExchange(){
//        return new TopicExchange(BindingName.EXCHANGE_NAME,true,false);
//    }
//
//    @Bean
//    public Binding mallMsgBinding(){
//        return BindingBuilder.bind(mallMsgQueue()).to(mallMsgExchange()).with(BindingName.ROUTING_KEY_NAME);
//    }

}
