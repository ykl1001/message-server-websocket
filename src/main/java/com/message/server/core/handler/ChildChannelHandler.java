package com.message.server.core.handler;

import com.message.server.core.config.NettyConfig;
import com.message.server.websocket.WebSocketHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 连接管道初始化配置
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/5/30 08:33
 */
@Component
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

    @Autowired
    private NettyConfig nettyConfig;

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        ChannelPipeline pipeline = sc.pipeline();

        // HttpServerCodec：将请求和应答消息解码为HTTP消息
        pipeline.addLast("http-codec", new HttpServerCodec());

        // HttpObjectAggregator：将HTTP消息的多个部分合成一条完整的HTTP消息
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));

        // ChunkedWriteHandler：向客户端发送HTML5文件
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());

        // 配置通道处理  来进行业务处理
        pipeline.addLast(webSocketHandler);

        pipeline.addLast(new LengthFieldBasedFrameDecoder(nettyConfig.getMaxFrameLength(), 0, 2, 0, 2));

        // 进行设置心跳检测，8分钟无交流
        pipeline.addLast(new IdleStateHandler(0, 0, 60 * 8, TimeUnit.SECONDS));
        pipeline.addLast(new IdleStateTriggerHandler());

        pipeline.addLast(new HeartBeatServerHandler());

        pipeline.addLast("decoder", new StringDecoder());

        pipeline.addLast("encoder", new StringEncoder());
    }
}
