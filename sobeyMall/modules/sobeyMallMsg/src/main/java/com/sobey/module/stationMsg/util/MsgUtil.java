package com.sobey.module.stationMsg.util;

import com.sobey.module.stationMsg.model.MallMsg;
import com.sobey.module.stationMsg.mq.producer.MsgProducer;
import com.sobey.module.stationMsg.socketIo.SocketIoEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author WCY
 * @CreateTime 2020/4/22 10:49
 */
@Component
public class MsgUtil {

    private static final Logger log = LoggerFactory.getLogger(MsgUtil.class);

    @Autowired
    private MsgProducer msgProducer;
    @Autowired
    private SocketIoEventHandler socketIoEventHandler;

    @Async("executors")
    public void sendMsg(List<MallMsg> msgs){
        try {
            if (null == msgs || msgs.size() == 0){
                return;
            }
            for (MallMsg msg : msgs) {
                msgProducer.creatMsg(msg);
            }
        } catch (Exception e) {
            log.error("消息发送异常",e);
        }
    }

}
