package com.message.server.core.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

/**
 * Http握手处理
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/5/29 13:35
 */
public class HttpRequestHandler {
    private static final String URI = "websocket";
    private static final String UPGRADE = "Upgrade";
    private static final int HTTP_CODE_SUCCESS = 200;
    private WebSocketServerHandshaker wssh;

    /**
     * wetsocket第一次连接握手
     *
     * @param ctx
     * @param msg
     */
    public void doHandlerHttpRequest(ChannelHandlerContext ctx, HttpRequest msg) {
        // http 解码失败
        if (!msg.getDecoderResult().isSuccess() || (!URI.equals(msg.headers().get(UPGRADE)))) {
            this.sendHttpResponse(ctx, (FullHttpRequest) msg, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
        }

        //可以获取msg的uri来判断
        String uri = msg.getUri();
        if (!uri.substring(1).equals(URI)) {
            ctx.close();
        }

        ctx.attr(AttributeKey.valueOf("type")).set(uri);

        //可以通过url获取其他参数
        WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory("ws://" + msg.headers().get("Host") + "/" + URI + "", null, false);
        wssh = factory.newHandshaker(msg);
        if (wssh == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        }

        //进行连接
        wssh.handshake(ctx.channel(), (FullHttpRequest) msg);
    }

    /**
     * 发送http返回消息
     *
     * @param ctx
     * @param req
     * @param res
     */
    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.getStatus().code() != HTTP_CODE_SUCCESS) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != HTTP_CODE_SUCCESS) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 关闭连接
     *
     * @param ch
     * @param msg
     */
    public void closeChannel(Channel ch, CloseWebSocketFrame msg) {
        wssh.close(ch, msg);
    }
}
