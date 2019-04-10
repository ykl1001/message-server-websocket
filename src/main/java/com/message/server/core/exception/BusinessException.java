package com.message.server.core.exception;

/**
 * 业务异常
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/6/19 15:52
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
}
