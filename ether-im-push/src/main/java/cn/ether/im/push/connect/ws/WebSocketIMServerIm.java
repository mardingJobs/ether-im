package cn.ether.im.push.connect.ws;

import cn.ether.im.push.connect.ImPushServer;
import cn.ether.im.push.connect.ws.codec.WebSocketMessageDecoder;
import cn.ether.im.push.connect.ws.codec.WebSocketMessageEncoder;
import cn.ether.im.push.handler.ImChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * * @Author: Martin
 * * @Date    2024/9/3 14:28
 * * @Description
 **/
@Slf4j
@Component
@ConditionalOnProperty(prefix = "websocket", value = "enable", havingValue = "true", matchIfMissing = true)
public class WebSocketIMServerIm implements ImPushServer {


    private boolean ready = false;

    @Value("${websocket.port}")
    private int port;

    private NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);

    private NioEventLoopGroup workerGroup = new NioEventLoopGroup();


    /**
     * @return
     */
    @Override
    public boolean ready() {
        return ready;
    }

    /**
     *
     */
    @Override
    public void start() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 256)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    /**
                     * This method will be called once the {@link Channel} was registered. After the method returns this instance
                     * will be removed from the {@link ChannelPipeline} of the {@link Channel}.
                     *
                     * @param ch the {@link Channel} which was registered.
                     * @throws Exception is thrown if an error occurs. In that case it will be handled by
                     *                   {@link #exceptionCaught(ChannelHandlerContext, Throwable)} which will by default close
                     *                   the {@link Channel}.
                     */
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new IdleStateHandler(20, 0, 0))
                                .addLast("http-codec", new HttpServerCodec())
                                .addLast("aggregator", new HttpObjectAggregator(65535))
                                .addLast("http-chunked", new ChunkedWriteHandler())
                                .addLast(new WebSocketServerProtocolHandler("/im"))
                                .addLast(new WebSocketMessageDecoder())
                                .addLast(new WebSocketMessageEncoder())
                                .addLast(new ImChannelHandler());
                    }
                });


        try {
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            this.ready = true;
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    log.info("WebSocketServer.start|websocket server 初始化完成,端口：{}", port);
                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    @Override
    public void shutdown() {
        if (bossGroup != null && !bossGroup.isShuttingDown()) {
            bossGroup.shutdownGracefully();
        }

        if (workerGroup != null && !workerGroup.isShuttingDown()) {
            workerGroup.shutdownGracefully();
        }
    }
}
