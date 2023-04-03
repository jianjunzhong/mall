package com.jjzhong.mall.exception;

/**
 * 自定义异常类
 */
public class MallException extends RuntimeException {
    private final Integer code;
    private final String message;

    public MallException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public MallException(MallExceptionEnum exceptionEnum) {
        this(exceptionEnum.getCode(), exceptionEnum.getMessage());
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
