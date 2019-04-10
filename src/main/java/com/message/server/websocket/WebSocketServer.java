package com.message.server.websocket;

import com.message.server.core.config.NettyConfig;
import com.message.server.core.handler.ChildChannelHandler;
import com.message.server.core.util.BaseDynamicRunnable;
import com.message.server.core.util.ThreadUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

/**
 * 服务启动监听器
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/5/28 10:53
 */
@Component
public class WebSocketServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 申明线程池
     */
    private static ExecutorService pool = ThreadUtil.getExecutorService();

    @Autowired
    private NettyConfig nettyConfig;

    @Autowired
    private ChildChannelHandler childChannelHandler;

    private void startServer() {
        // WS服务运行端口
        int port = nettyConfig.getPort();

        //服务端需要2个线程组  boss处理客户端连接  work进行客服端连接之后的处理
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();

        try {
            //服务器 配置
            ServerBootstrap sbs = new ServerBootstrap().group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(childChannelHandler)
                    .option(ChannelOption.SO_BACKLOG, nettyConfig.getMaxQueueSize())
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            //绑定端口  开启事件驱动
            ChannelFuture future = sbs.bind(port).sync();

            LOGGER.info("---------->WS服务器启动成功，端口：{}<----------", port);

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    public void init() {
        //在线程池中执行 netty server 服务器
        pool.execute(new BaseDynamicRunnable() {
            @Override
            public void doSth() {
                startServer();
            }
        });
    }
}
