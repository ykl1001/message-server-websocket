package com.message.server.model;

import com.message.server.core.enums.WSTypeEnum;
import lombok.Data;

/**
 * WebSocket消息实体
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/6/3 17:05
 */
@Data
public class WsMessage {
    private WSTypeEnum type;
    private String userId;
    private ReturnT<?> data;

    public WsMessage() {
    }

    public WsMessage(WSTypeEnum type) {
        this.type = type;
    }

    public WsMessage(WSTypeEnum type, ReturnT<?> data) {
        this.type = type;
        this.data = data;
    }

    public WSTypeEnum getType() {
        return type;
    }

    public void setType(WSTypeEnum type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ReturnT<?> getData() {
        return data;
    }

    public void setData(ReturnT<?> data) {
        this.data = data;
    }
}
