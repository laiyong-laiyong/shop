package com.sobey.module.config.socketio;

import com.corundumstudio.socketio.AckMode;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.sobey.module.stationMsg.enumeration.MsgBasicType;
import com.sobey.module.stationMsg.listener.SocketIoExceptionListener;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@SpringBootConfiguration
public class NettySocketIoConfig {

    private static final Logger log = LoggerFactory.getLogger(NettySocketIoConfig.class);

    @Value("${socket-io.port}")
    private int port;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration configuration = new Configuration();
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        configuration.setSocketConfig(socketConfig);
        configuration.setContext("/sobey.ly.mall");
        configuration.setPort(port);
        configuration.setAckMode(AckMode.AUTO);
        configuration.setPingInterval(50000);//ping时间间隔
        configuration.setPingTimeout(150000);//ping超时时间
        configuration.setBossThreads(1);
        configuration.setWorkerThreads(100);
        configuration.setExceptionListener(new SocketIoExceptionListener());
        //第一次连接时需要校验
        configuration.setAuthorizationListener(handShakeData -> {
            String type = handShakeData.getSingleUrlParam("type");
            if (!MsgBasicType.isTrueCode(type)) {
                return false;
            }
            if (MsgBasicType.System.getCode().equalsIgnoreCase(type)) {
                try {
                    String userCode = handShakeData.getSingleUrlParam("userCode");
                    String sign = handShakeData.getSingleUrlParam("sign");
                    if (StringUtils.isBlank(userCode) || StringUtils.isBlank(sign)) {
                        return false;
                    }
                    sign = URLDecoder.decode(sign,"UTF-8");
                    //鉴定sign
                    String auth = userCode+type;
                    String result = Base64.encodeBase64String(auth.getBytes(StandardCharsets.UTF_8));
                    if (!sign.equals(result)){
                        return false;
                    }
                } catch (Exception e) {
                    log.info("socket.io 连接校验出错", e);
                    return false;
                }
            }
            return true;
        });
        configuration.setMaxFramePayloadLength(1024 * 1024);//设置最大每帧处理数据的长度 这里设置1M
        configuration.setMaxHttpContentLength(1024 * 1024);//设置http交互最大内容长度 这里设置1M
        return new SocketIOServer(configuration);
    }


    /**
     * 用于扫描netty-socket.io的注解
     *
     * @return
     */
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketIOServer) {
        return new SpringAnnotationScanner(socketIOServer);
    }

}
