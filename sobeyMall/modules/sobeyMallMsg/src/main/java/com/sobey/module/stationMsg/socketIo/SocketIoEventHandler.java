package com.sobey.module.stationMsg.socketIo;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.sobey.module.stationMsg.entity.WorkOrderMsg;
import com.sobey.module.stationMsg.enumeration.MsgBasicType;
import com.sobey.module.stationMsg.model.MallMsg;
import com.sobey.module.stationMsg.model.MallMsgBroadcast;
import com.sobey.module.stationMsg.model.MallMsgProclamation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SocketIoEventHandler {

    private static final Logger log = LoggerFactory.getLogger(SocketIoEventHandler.class);

    //客户端集合 初始化合适的集合长度，避免频繁扩容
    private final static ConcurrentHashMap<String, ConcurrentHashMap<String, SocketIOClient>> clients = new ConcurrentHashMap<>(64);
    //广播频道集合
    private final static ConcurrentHashMap<String, SocketIOClient> broadcastClients = new ConcurrentHashMap<>(64);
    //公告频道集合
    private final static ConcurrentHashMap<String, SocketIOClient> announcementClients = new ConcurrentHashMap<>(64);

    /**
     * 连接监听
     *
     * @param client
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        if (null != client) {
            String type = client.getHandshakeData().getSingleUrlParam("type").trim();
            String sessionId = client.getSessionId().toString();
            if (MsgBasicType.System.getCode().equals(type)) {
                String userCode = client.getHandshakeData().getSingleUrlParam("userCode").trim();
                ConcurrentHashMap<String, SocketIOClient> socketIos = SocketIoEventHandler.clients.getOrDefault(userCode, new ConcurrentHashMap<>());
                socketIos.put(sessionId,client);
                clients.put(userCode,socketIos);
                log.info("用户{}已连接,sessionId:{}", userCode, sessionId);
            }
            //广播类型
            if (MsgBasicType.Announcement.getCode().equals(type)) {
                broadcastClients.put(sessionId, client);
                log.info("有新连接加入,sessionId:{}", sessionId);
            }
            //公告类型
            if (MsgBasicType.Proclamation.getCode().equals(type)) {
                announcementClients.put(sessionId, client);
                log.info("有新连接加入,sessionId:{}", sessionId);
            }
            client.sendEvent("msg", sessionId);
        } else {
            log.error("client为null");
        }
    }

    /**
     * 断开监听
     * @param client
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        if (null != client) {
            String type = client.getHandshakeData().getSingleUrlParam("type").trim();
            String sessionId = client.getSessionId().toString();
            if (MsgBasicType.System.getCode().equals(type)) {
                String userCode = client.getHandshakeData().getSingleUrlParam("userCode").trim();
                log.info("用户{}断开连接,sessionId:{}", userCode, sessionId);
                ConcurrentHashMap<String, SocketIOClient> socketIos = clients.getOrDefault(userCode, new ConcurrentHashMap<>());
                socketIos.remove(sessionId);
                if (socketIos.size() == 0){
                    clients.remove(userCode);
                    return;
                }
                clients.put(userCode,socketIos);
                return;
            }
            //广播类型
            if (MsgBasicType.Announcement.getCode().equals(type)) {
                broadcastClients.remove(sessionId);
                log.info("有一连接断开,sessionId:{}", sessionId);
                return;
            }
            //公告类型
            if (MsgBasicType.Proclamation.getCode().equals(type)) {
                announcementClients.remove(sessionId);
                log.info("有一连接断开,sessionId:{}", sessionId);
            }
        } else {
            log.error("client为null");
        }
    }

    /**
     * 接收客户端发送的消息
     */
    @OnEvent("sendMsg")
    public void onSendMsg(SocketIOClient client, AckRequest ackRequest, Object data) {
        log.info("收到来自客户端消息:{}", data);
        if (ackRequest.isAckRequested()) {
            ackRequest.sendAckData("回复ack", "ok");
        }
    }

    /**
     * 发送消息到个人
     *
     * @param msg
     */
    public void sendMsgToPerson(MallMsg msg) {

        if (null == msg) {
            return;
        }
        String accountId = msg.getAccountId();
        ConcurrentHashMap<String, SocketIOClient> socketIoClients = clients.get(accountId);
        if (null == socketIoClients || socketIoClients.size() == 0) {
            log.info("用户{}不在线", accountId);
            return;
        }
        for (Map.Entry<String, SocketIOClient> clientEntry : socketIoClients.entrySet()) {
            SocketIOClient socketIOClient = clientEntry.getValue();
            if (null != socketIOClient) {
                socketIOClient.sendEvent(clientEntry.getKey()+"system", msg);
            }
        }
    }

    /**
     * 发送文本消息
     * @param accountId
     * @param msg
     */
    public void sendTextMsgToPerson(String accountId,String msg){
        ConcurrentHashMap<String, SocketIOClient> clientMap = clients.get(accountId);
        if (null == clientMap || clientMap.size() == 0) {
            log.info("用户{}不在线", accountId);
            return;
        }
        for (Map.Entry<String, SocketIOClient> clientEntry : clientMap.entrySet()) {
            SocketIOClient socketIOClient = clientEntry.getValue();
            if (null != socketIOClient) {
                socketIOClient.sendEvent(clientEntry.getKey()+"system", msg);
            }
        }
    }

    /**
     * 发送广播类型的消息
     *
     * @param msg
     */
    public void sendMsgToBroadCast(MallMsgBroadcast msg) {
        if (broadcastClients.size() == 0) {
            return;
        }
        for (Map.Entry<String, SocketIOClient> entry : broadcastClients.entrySet()) {
            SocketIOClient client = entry.getValue();
            if (null != client) {
                client.sendEvent("broadcastMsg", msg);
            }
        }
    }

    /**
     * 发送公告类消息
     *
     * @param msg
     */
    public void sendMsgToAnnouncement(MallMsgProclamation msg) {
        if (announcementClients.size() == 0) {
            return;
        }
        for (Map.Entry<String, SocketIOClient> entry : announcementClients.entrySet()) {
            SocketIOClient client = entry.getValue();
            if (null != client) {
                client.sendEvent("announcementMsg", msg);
            }
        }
    }

    /**
     * 撤销公告内容
     *
     * @param uuid
     */
    public void revokeAnnouncementMsg(String uuid) {

        if (announcementClients.size() == 0) {
            return;
        }
        for (Map.Entry<String, SocketIOClient> entry : announcementClients.entrySet()) {
            SocketIOClient client = entry.getValue();
            if (null != client) {
                client.sendEvent("revokeAnnouncementMsg", uuid);
            }
        }
    }

    /**
     * 工单在线聊天
     * @param msg
     */
    public void onlineChat(WorkOrderMsg msg) {
        if (null == msg) {
            return;
        }
        List<String> userCodes = new ArrayList<>();
        userCodes.add(msg.getSourceUserCode());
        userCodes.add(msg.getTargetUserCode());
        for (String userCode : userCodes) {
            if (StringUtils.isBlank(userCode)) {
                continue;
            }
            ConcurrentHashMap<String, SocketIOClient> socketIoClients = clients.get(userCode);
            if (null == socketIoClients || socketIoClients.size() == 0) {
                log.info("用户{}不在线", userCode);
                continue;
            }
            for (Map.Entry<String, SocketIOClient> entry : socketIoClients.entrySet()) {
                SocketIOClient client = entry.getValue();
                client.sendEvent(entry.getKey()+"chat", msg);
            }
        }
    }

    /**
     * 推送当月出售商品数与交易额信息
     * @param map num:出售商品数 amount:交易额
     */
    public void pushStatisticsResult(Map<String,Object> map){
        if (map == null || map.size() == 0){
            return;
        }
        if (clients.size() > 0){
            for (Map.Entry<String, ConcurrentHashMap<String, SocketIOClient>> entry : clients.entrySet()) {
                if (entry.getValue() != null && entry.getValue().size() > 0){
                    for (Map.Entry<String, SocketIOClient> clientEntry : entry.getValue().entrySet()) {
                        SocketIOClient client = clientEntry.getValue();
                        client.sendEvent(clientEntry.getKey()+"statistic",map);
                    }
                }
            }
        }
    }

}
