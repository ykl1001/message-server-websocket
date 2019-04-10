package com.message.server.core.enums;

/**
 * WebSocket 操作类型枚举
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/6/5 08:40
 */
public enum WSTypeEnum {
    /**
     * LOGIN 登录消息
     * ERROR 异常消息
     * MESSAGE 其他消息推送
     * DOWNLINE 下线
     */
    LOGIN, ERROR, MESSAGE, DOWNLINE
}
