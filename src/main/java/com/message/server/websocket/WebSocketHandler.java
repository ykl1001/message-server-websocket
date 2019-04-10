package com.message.server.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.message.server.core.enums.WSTypeEnum;
import com.message.server.core.handler.HttpRequestHandler;
import com.message.server.core.util.GlobalUserUtil;
import com.message.server.model.ReturnCode;
import com.message.server.model.ReturnT;
import com.message.server.model.WsMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * WS客户端连接、消息处理
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/5/28 10:53
 */
@Component
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);

    private static HttpRequestHandler httpRequestHandler;

    static {
        httpRequestHandler = new HttpRequestHandler();
    }

    /**
     * 活跃的通道  也可以当作用户连接上客户端进行使用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LOGGER.info("----------> 有客户端连接了，id={}", ctx.channel().id());
//        GlobalUserUtil.channels.add(ctx.channel());
    }

    /**
     * 不活跃的通道  就说明用户失去连接
     *
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        GlobalUserUtil.channels.remove(ctx.channel());
    }

    /**
     * channel 通道 Read 读取 Complete 完成 在通道读取完成后会在这个方法里通知，对应可以做刷新操作 flush
     *
     * @param ctx
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * 收发消息处理
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpRequest) {
            httpRequestHandler.doHandlerHttpRequest(ctx, (HttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            doHandlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    /**
     * websocket消息处理
     *
     * @param ctx
     * @param msg
     */
    private void doHandlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg) {
        //判断msg 是哪一种类型  分别做出不同的反应
        if (msg instanceof CloseWebSocketFrame) {
            LOGGER.info("---------->关闭<----------");
            httpRequestHandler.closeChannel(ctx.channel(), (CloseWebSocketFrame) msg);
            return;
        }
        if (msg instanceof PingWebSocketFrame) {
            LOGGER.info("---------->ping<----------");
            PongWebSocketFrame pong = new PongWebSocketFrame(msg.content().retain());
            ctx.channel().writeAndFlush(pong);
            return;
        }
        if (!(msg instanceof TextWebSocketFrame)) {
            LOGGER.info("---------->不支持二进制<----------");
            throw new UnsupportedOperationException("不支持二进制");
        }

        WsMessage wsMessage = null;
        try {
            // 客户端输入消息内容
            String message = ((TextWebSocketFrame) msg).text();

            LOGGER.info("---------->接到前端消息，Msg={}<----------", message);

            wsMessage = JSON.parseObject(message, WsMessage.class);
        } catch (Exception e) {
            LOGGER.error("---------->消息内容解析异常，ERROR={}<----------", e);
        }

        // 数据格式不正确
        if (wsMessage == null) {
            ReturnT<String> jsonResult = new ReturnT<>(ReturnCode.DATA_FORMAT_ERROR);
            WsMessage resMsgObj = new WsMessage(WSTypeEnum.ERROR, jsonResult);
            TextWebSocketFrame sendText = new TextWebSocketFrame(JSONObject.toJSONString(resMsgObj));
            ctx.channel().writeAndFlush(sendText);
            return;
        }

        WSTypeEnum type = wsMessage.getType();
        ReturnT<String> result;
        WsMessage message = new WsMessage(type);

        switch (type) {
            case LOGIN:
                // 获取用户信息
                String userId = wsMessage.getUserId();
                if (userId != null) {
                    // 登录互踢，判断当前登录人是否登录过，如果登录过则让前一个登录实体下线
                    Channel userCh = GlobalUserUtil.getChannelByUserId(userId);
                    if (userCh != null) {
                        WsMessage message1 = new WsMessage();
                        message1.setType(WSTypeEnum.DOWNLINE);
                        message1.setUserId(wsMessage.getUserId());
                        // 发送下线消息
                        userCh.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(message1)));
                        // 移除连接实例
                        GlobalUserUtil.channels.remove(userCh);

                        LOGGER.info("---------->用户（{}）在其他地方登录，被迫下线！<----------", userId);
                    }

                    ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);
                    // TODO 添加部门标识
                    GlobalUserUtil.channels.add(ctx.channel());
                    result = new ReturnT<>(ReturnCode.WS_LOGIN_SUCCESS);

                    LOGGER.info("---------->用户（{}）登录成功！<----------", userId);
                } else {
                    LOGGER.error("---------->token={}，校验失败！<----------", wsMessage.getUserId());

                    // token验证失败
                    result = new ReturnT<>(ReturnCode.WS_LOGIN_FAIL);
                }
                break;
            default:
                LOGGER.error("---------->不存在的消息类型，type={}<----------", type.name());

                message.setType(WSTypeEnum.ERROR);
                result = new ReturnT<>(ReturnCode.SYSTEM_ERROR);
                break;
        }

        message.setData(result);

        TextWebSocketFrame sendText = new TextWebSocketFrame(JSONObject.toJSONString(message));
        ctx.channel().writeAndFlush(sendText);
    }
}