package com.sobey.module.stationMsg.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ExceptionListener;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SocketIoExceptionListener implements ExceptionListener {

    private static final Logger log = LoggerFactory.getLogger(SocketIoExceptionListener.class);

    @Override
    public void onEventException(Exception e, List<Object> args, SocketIOClient client) {
        log.error("socket.io Event异常:{}",e.getMessage());
    }

    @Override
    public void onDisconnectException(Exception e, SocketIOClient client) {
        log.error("socket.io Disconnect异常:{}",e.getMessage());
    }

    @Override
    public void onConnectException(Exception e, SocketIOClient client) {
        log.error("socket.io Connect异常:{}",e.getMessage());

    }

    @Override
    public void onPingException(Exception e, SocketIOClient client) {
        log.error("socket.io Ping异常:{}",e.getMessage());
    }

    @Override
    public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
        if("Connection reset by peer".equalsIgnoreCase(e.getMessage())){
            return true;
        }
        log.error("socket.io异常:{}",e.getMessage());
        return true;
    }
}
