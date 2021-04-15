package com.sobey.module.mq.constant;

/**
 * @Description 保存交换器队列等名称
 * @Author WuChenYang
 * @CreateTime 2020-03-21 15:40
 */
public class BindingConstant {

    public static final String CHARGE_DIRECT_EXCHANGE = "sobey.charge";
    public static final String ROUTING_KEY = "sobeycharge";
    public static final String CHARGE_QUEUE = "sobeycharge";


    public static long FREEZING_TIME = 1000*60*60*24*14L;//冻结时长,14天
    public static final String READY_DESTROY_QUEUE="ready-destroy-queue";//冻结计时队列，准备销毁服务，冻结期过销毁
    public static final String READY_DESTROY_EXCHANGE="ready-destroy-exchange";
    public static final String READY_DESTROY_ROUTING_KEY="ready-destroy-routing-key";
    public static final String DEAD_EXCHANGE_DESTROY="dead-exchange-destroy";//销毁队列的死信交换机
    public static final String DEAD_ROUTING_KEY_DESTROY="dead-routing-key-destroy";//销毁队列的死信路由key
    public static final String DESTROY_QUEUE="destroy-service";//真正执行服务销毁的队列

    //订单支付有效时间倒计时相关队列
    public static long ORDER_EXPIRE_TIME = 1000*60*60*24*7L;//有效期7天
    public static final String READY_ORDER_EXPIRE_QUEUE="ready-order-expire-queue";//待支付订单计时队列
    public static final String READY_ORDER_EXPIRE_EXCHANGE="ready-order-expire-exchange";
    public static final String READY_ORDER_EXPIRE_ROUTING_KEY="ready-order-expire-routing-key";
    public static final String DEAD_ORDER_EXPIRE_EXCHANGE="dead-order-expire-exchange";//订单过期队列的死信交换机
    public static final String DEAD_ORDER_EXPIRE_ROUTING_KEY="dead-order-expire-routing-key";//订单过期队列的死信路由key
    public static final String ORDER_EXPIRE_QUEUE="order-expire-queue";//订单过期队列

}
