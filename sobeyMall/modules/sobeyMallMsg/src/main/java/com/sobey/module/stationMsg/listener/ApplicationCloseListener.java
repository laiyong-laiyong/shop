package com.sobey.module.stationMsg.listener;

import com.corundumstudio.socketio.SocketIOServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * @Author WCY
 * @CreateTime 2020/4/24 11:36
 * 容器关闭监听
 */
@Component
public class ApplicationCloseListener implements ApplicationListener<ContextClosedEvent> {

    private static final Logger log = LoggerFactory.getLogger(ApplicationCloseListener.class);

    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        socketIOServer.stop();
        String applicationName = event.getApplicationContext().getApplicationName();
        log.info("container "+applicationName+" closed");
    }
}
