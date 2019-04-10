package com.message.server.core.exception;

import com.message.server.model.ReturnCode;
import com.message.server.model.ReturnT;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局异常处理
 *
 * @author yangdaxin@lcservis.com
 * @version 创建时间 2018/6/13 10:11
 */
@ControllerAdvice
public class GlobalException extends Exception {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalException.class);

    /**
     * 全局异常捕捉处理
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ReturnT<String> errorHandler(Exception e) {
        LOGGER.error("未知异常", e);
        return new ReturnT<>(ReturnCode.SYSTEM_ERROR, "未知异常");
    }

    /**
     * 校验异常捕捉处理
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ReturnT<String> validatedErrorHandler(MethodArgumentNotValidException e) {
        LOGGER.error("字段校验异常", e);

        BindingResult result = e.getBindingResult();
        List<ObjectError> errors = result.getAllErrors();

        List<String> errorList = new ArrayList<>();
        for (ObjectError error : errors) {
            errorList.add(error.getDefaultMessage());
        }

        return new ReturnT<>(ReturnCode.DATA_FORMAT_ERROR, "字段校验异常", StringUtils.join(errorList, ";"));
    }

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public ReturnT<String> businessExceptionHandler(Exception e) {
        LOGGER.error("业务异常", e);
        return new ReturnT<>(ReturnCode.SYSTEM_ERROR, "业务异常");
    }
}
