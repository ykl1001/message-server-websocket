package com.message.server.core.util;

import com.alibaba.fastjson.JSONObject;
import com.message.server.core.enums.MsgTypeEnum;
import com.message.server.core.enums.WSTypeEnum;
import com.message.server.model.ReturnCode;
import com.message.server.model.ReturnT;
import com.message.server.model.SiteMessage;
import com.message.server.model.WsMessage;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 用户全局配置
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/5/28 10:50
 */
public class GlobalUserUtil {
    /**
     * 保存全局的  连接上服务器的客户
     */
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 根据用户id获取用户WS连接通道实例
     *
     * @param userId 用户id
     * @return
     */
    public static Channel getChannelByUserId(String userId) {
        Channel ch = null;
        for (Channel channel : GlobalUserUtil.channels) {
            if (userId.equals(channel.attr(AttributeKey.valueOf("userId")).get())) {
                ch = channel;
                break;
            }
        }
        return ch;
    }

    /**
     * 消息发送
     *
     * @param siteMessage 消息体
     * @param msgType     消息类型
     */
    public static void sendMessageByUserId(SiteMessage siteMessage, MsgTypeEnum msgType) {
        Channel channel = GlobalUserUtil.getChannelByUserId(siteMessage.getRefToUserId());
        if (channel != null) {
            JSONObject msgBody = new JSONObject(6);
            msgBody.put("id", siteMessage.getId());
            msgBody.put("time", siteMessage.getCreateTime());
            msgBody.put("title", siteMessage.getTitle());
            msgBody.put("categoryId", siteMessage.getCategoryId());
            msgBody.put("categoryName", siteMessage.getCategoryName());
            msgBody.put("type", msgType);

            ReturnT<JSONObject> returnT = new ReturnT<>(ReturnCode.SUCCESS, "流程消息", msgBody);
            WsMessage message = new WsMessage(WSTypeEnum.MESSAGE, returnT);

            channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(message)));
        }
    }
}
