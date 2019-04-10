package com.message.server.core.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebSocket心跳配置
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/5/29 13:04
 */
@ChannelHandler.Sharable
public class IdleStateTriggerHandler extends ChannelHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdleStateTriggerHandler.class);

    /**
     * 这里是保持服务器与客户端长连接  进行心跳检测 避免连接断开
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent stateEvent = (IdleStateEvent) evt;
            PingWebSocketFrame ping = new PingWebSocketFrame();
            PongWebSocketFrame pong = new PongWebSocketFrame();
            switch (stateEvent.state()) {
                //读空闲（服务器端）
                case READER_IDLE:
                    LOGGER.info("---------->【" + ctx.channel().remoteAddress() + "】读空闲（服务器端）<----------");
                    ctx.writeAndFlush(ping);
                    break;
                //写空闲（客户端）
                case WRITER_IDLE:
                    LOGGER.info("---------->【" + ctx.channel().remoteAddress() + "】写空闲（客户端）<----------");
                    ctx.writeAndFlush(ping);
                    break;
                case ALL_IDLE:
                    LOGGER.info("---------->【" + ctx.channel().remoteAddress() + "】读写空闲<----------");
                    break;
                default:
                    break;
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
