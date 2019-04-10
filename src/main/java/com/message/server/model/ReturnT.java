package com.message.server.model;

import java.io.Serializable;

/**
 * 统一数据返回
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/6/15 23:11
 */
public class ReturnT<T> implements Serializable {
    public static final long serialVersionUID = 1L;

    public static ReturnT<String> SUCCESS = new ReturnT<>(ReturnCode.SUCCESS);
    public static ReturnT<String> FAIL = new ReturnT<>(ReturnCode.SYSTEM_ERROR);

    private Integer code;
    private String message;
    private T result;

    public ReturnT(ReturnCode code) {
        this.setCode(code);
        this.setMessage(code.msg());
    }

    public ReturnT(ReturnCode code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    public ReturnT(ReturnCode code, String message, T data) {
        this.setCode(code);
        this.setMessage(message);
        this.setResult(data);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(ReturnCode code) {
        this.code = code.val();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
