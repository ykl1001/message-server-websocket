package com.message.server.model;

/**
 * 统一返回码
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/6/2 22:10
 */
public enum ReturnCode {
    /**
     * 服务器成功返回用户请求的数据【GET】
     */
    SUCCESS(200, "成功"),

    /**
     * token未授权
     */
    UNAUTHORIZED(401, "token未授权"),

    /**
     * 用户请求的格式不可得
     */
    DATA_FORMAT_ERROR(406, "数据格式不正确"),

    /**
     * 用户请求的资源被永久删除，且不会再得到的
     */
    DATA_NOT_EXIST(410, "数据不存在"),

    /**
     * 系统错误
     */
    SYSTEM_ERROR(500, "系统错误"),

    /**
     * WebSocket登录成功
     */
    WS_LOGIN_SUCCESS(1000, "WebSocket登录成功"),

    /**
     * WebSocket登录失败
     */
    WS_LOGIN_FAIL(1001, "WebSocket登录失败"),

    /**
     * 请求方法不存在
     */
    WS_TYPE_NOT_EXIST(1002, "请求类型不存在");

    private ReturnCode(Integer value, String msg) {
        this.val = value;
        this.msg = msg;
    }

    public Integer val() {
        return val;
    }

    public String msg() {
        return msg;
    }

    private Integer val;
    private String msg;
}
