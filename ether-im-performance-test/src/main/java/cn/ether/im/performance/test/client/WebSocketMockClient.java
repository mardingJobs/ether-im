package cn.ether.im.performance.test.client;

import cn.ether.im.common.enums.ImChatMessageType;
import cn.ether.im.common.enums.ImMessageType;
import cn.ether.im.common.enums.ImSystemMessageType;
import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.event.ImMessageEventType;
import cn.ether.im.common.model.message.*;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.util.JwtUtils;
import cn.ether.im.common.util.ThreadPoolUtils;
import cn.ether.im.performance.test.user.MockUser;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Date;
import java.util.Random;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/27 12:50
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Slf4j
public class WebSocketMockClient extends WebSocketClient {

    private MockUser mockUser;

    public WebSocketMockClient(MockUser mockUser, URI serverUri) {
        this(serverUri);
        this.mockUser = mockUser;
        String jsonString = JSON.toJSONString(mockUser);
        String token = JwtUtils.sign(mockUser.getUserId(), jsonString, 3600 * 24 * 7, "marding");
        this.addHeader("token", token);
    }

    public WebSocketMockClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        log.info("【{}】已连接", mockUser);
    }

    @Override
    public void onMessage(String message) {
        log.info("【{}】,message:{}", mockUser, message);
        ImMessage imMessage = ImMessage.parseObject(message);
        if (imMessage.getMessageType() == ImMessageType.SYSTEM) {
            ImSystemMessage systemMessage = (ImSystemMessage) imMessage;
            if (systemMessage.getSystemMessageType() == ImSystemMessageType.HB) {
                log.info("【{}】发送心跳", mockUser);
                sendMessage(new ImHeartbeatMessage());
            }
        } else if (imMessage.getMessageType() == ImMessageType.CHAT) {
            ImChatMessage chatMessage = (ImChatMessage) imMessage;
            log.info("【{}】收到对话消息：{}", mockUser, chatMessage);

            int sleepTimes = new Random().nextInt(1);
            try {
                log.info("【{}】模拟客户端返回触达事件延迟,时间：{}", mockUser, sleepTimes);
                Thread.sleep(sleepTimes * 1000);
                sendReachedEvent(chatMessage);
            } catch (Exception e) {
                log.error("【用户终端】处理消息异常", e);
            }

        }
    }


    @Override
    public void send(String text) {
        boolean open = myReconnect();
        if (open) {
            super.send(text);
        }
    }

    /**
     * 在连接断开时尝试重连
     *
     * @return
     */
    public boolean myReconnect() {
        if (this.isOpen()) {
            return true;
        }
        try {
            Thread.sleep(3000);
            log.info("【{}】重连服务器...", mockUser);
            if (this.getReadyState().equals(ReadyState.NOT_YET_CONNECTED)) {
                super.connectBlocking();
                return true;
            } else if (this.getReadyState().equals(ReadyState.CLOSING)
                    || this.getReadyState().equals(ReadyState.CLOSED)) {
                try {
                    this.reconnectBlocking();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        } catch (Exception e) {
            log.error("reconnect error ", e);
        }
        log.info("【{}】重连服务器状态：{}", mockUser, this.isOpen());
        return this.isOpen();
    }

    private void sendReachedEvent(ImChatMessage chatMessage) {
        ImMessageEvent messageEvent = new ImMessageEvent();
        messageEvent.setMessageId(chatMessage.getId());
        messageEvent.setEventTime(new Date().getTime());
        messageEvent.setTerminal(new ImUserTerminal(mockUser.getUserId(), ImTerminalType.valueOf(mockUser.getTerminalType())));
        messageEvent.setEventType(ImMessageEventType.REACHED);
        messageEvent.setChatMessageType(ImChatMessageType.PERSONAL);
        messageEvent.setSystemMessageType(ImSystemMessageType.EVENT);
        sendMessage(messageEvent);
        log.info("【{}】已回复触达事件,MessageId:{}", mockUser, messageEvent.getMessageId());
    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("【{}】onClose|{},code:{},reason:{}", mockUser, JSON.toJSONString(mockUser), code, reason);
        ThreadPoolUtils.execute(() -> {
            try {
                myReconnect();
            } catch (Exception e) {
                log.error("【{}】重连失败", mockUser, e);
            }
        });

    }

    @Override
    public void onError(Exception ex) {
        log.info("【{}】onError", mockUser, ex);
    }


    private void sendMessage(ImSystemMessage message) {
        this.send(JSON.toJSONString(message));
    }
}
