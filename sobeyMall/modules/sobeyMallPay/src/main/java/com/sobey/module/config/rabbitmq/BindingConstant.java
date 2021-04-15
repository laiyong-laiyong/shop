package com.sobey.module.config.rabbitmq;

/**
 * @Description 保存交换器队列等名称
 * @Author WuChenYang
 * @CreateTime 2020-03-21 15:40
 */
public class BindingConstant {

    public static long EXPIRE_TIME = 1000*60*60*24*30L;//代金券有效期,30天
    public static final String READY_VOUCHER_EXPIRE_QUEUE="ready-voucher-expire-queue";//代金券失效计时队列
    public static final String READY_VOUCHER_EXPIRE_EXCHANGE="ready-voucher-expire-exchange";
    public static final String READY_VOUCHER_EXPIRE_ROUTING_KEY="ready-voucher-expire-routing-key";
    public static final String DEAD_EXCHANGE_VOUCHER_EXPIRE="dead-exchange-voucher-expire";//代金券失效计时队列的死信交换机
    public static final String DEAD_ROUTING_KEY_VOUCHER_EXPIRE="dead-routing-key-voucher-expire";//代金券失效计时队列的死信路由key
    public static final String VOUCHER_EXPIRE_QUEUE="voucher-expire-queue";//真正执行代金券过期的队列

}
