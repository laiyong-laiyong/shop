package com.sobey.module.stationMsg.socketIo;

import com.corundumstudio.socketio.SocketIOServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;

@SpringBootConfiguration
public class NettySocketIoApp implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(NettySocketIoApp.class);

    @Autowired
    private SocketIOServer socketIOServer;

    public static void main(String[] args) {
        SpringApplication.run(NettySocketIoApp.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        socketIOServer.start();
        log.info("socket.io service started success");
    }
}
