package com.message.server.core.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 链接异常处理
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/5/29 13:08
 */
@ChannelHandler.Sharable
public class HeartBeatServerHandler extends ChannelHandlerAdapter {
    protected static final Logger LOGGER = LoggerFactory.getLogger(HeartBeatServerHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("---------->连接异常，ERROR={}<----------", ctx.channel().id());
        ctx.close();
        ctx.channel().close();
    }
}
