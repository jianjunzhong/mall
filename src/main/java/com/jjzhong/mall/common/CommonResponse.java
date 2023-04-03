package com.jjzhong.mall.common;

import com.jjzhong.mall.exception.MallExceptionEnum;

/**
 * 统一响应类
 * @param <T> 返回的 data 的数据类型
 */
public class CommonResponse<T> {
    private Integer status;
    private String msg;
    private T data;

    private static final int OK_CODE = 10000;
    private static final String OK_MSG = "SUCCESS";

    public CommonResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public CommonResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public CommonResponse() {
        this(OK_CODE, OK_MSG);
    }

    public static <T> CommonResponse<T> success() {
        return new CommonResponse<>(OK_CODE, OK_MSG);
    }

    public static <T> CommonResponse<T> success(T result) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setData(result);
        return response;
    }

    public static <T> CommonResponse<T> error(Integer code, String msg) {
        return new CommonResponse<>(code, msg);
    }

    public static <T> CommonResponse<T> error(MallExceptionEnum ex) {
        return new CommonResponse<>(ex.getCode(), ex.getMessage());
    }

    @Override
    public String toString() {
        return "ApiRestResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
