package cn.ether.im.performance.test.client;

import cn.ether.im.common.enums.ImSystemMessageType;
import cn.ether.im.common.model.message.ImHeartbeatMessage;
import cn.ether.im.common.model.message.ImSystemMessage;
import cn.ether.im.common.util.JwtUtils;
import cn.ether.im.performance.test.user.MockUser;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

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
        log.info("onOpen|{}", JSON.toJSONString(mockUser));
    }

    @Override
    public void onMessage(String message) {
        log.info("onMessage|{},message:{}", JSON.toJSONString(mockUser), message);
        ImSystemMessage systemMessage = ImSystemMessage.parseObject(message);
        if (systemMessage.getType() == ImSystemMessageType.HB) {
            this.send(JSON.toJSONString(new ImHeartbeatMessage()));
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("onClose|{},code:{},reason:{}", JSON.toJSONString(mockUser), code, reason);
    }

    @Override
    public void onError(Exception ex) {
        log.info("onError|{}", JSON.toJSONString(mockUser), ex.getCause());
    }
}
