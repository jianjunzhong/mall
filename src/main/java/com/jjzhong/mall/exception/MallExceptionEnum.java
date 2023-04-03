package com.jjzhong.mall.exception;

/**
 * 异常枚举类
 */
public enum MallExceptionEnum {
    NEED_USER_NAME(10001, "用户名不能为空"),
    USER_NAME_EXISTED(10002, "用户名已存在"),
    INSERT_FAILED(10003, "插入失败，请重试"),
    NEED_PASSWORD(10004, "密码不能为空"),
    PASSWORD_TOO_SHORT(10005, "密码长度不能小于8位"),
    NOT_VALID_USER_NAME_OR_PASSWORD(10006, "用户名或密码错误"),
    NEED_LOGIN(10007, "未登陆"),
    UPDATE_FAILED(10008, "更新失败"),
    NEED_ADMIN(10009, "无管理员权限"),
    NAME_EXISTED(10010, "名称已存在"),
    REQUEST_PARAM_ERROR(10011, "参数错误"),
    DELETE_FAILED(10012, "删除失败"),
    ADD_FAILED(10013, "新增失败"),
    DIR_CREATE_FAILED(10014, "文件夹创建失败"),
    UPLOAD_FAILED(10015, "上传失败"),
    NOT_SELL(10016, "商品不存在或未上架"),
    NOT_ENOUGH(10017, "库存不足"),
    CART_EMPTY_OR_NOT_SELECTED(10018, "购物车为空或未选择商品"),
    ORDER_STATUS_INCORRECT(10019, "订单状态异常"),
    ORDER_NOT_FOUND(10020, "未找到订单"),
    ORDER_NOT_MATCH(10021, "订单和用户不匹配"),
    INCORRECT_EMAIL_ADDRESS(10022, "Email格式错误"),
    EMAIL_REGISTERED(10023, "Email已被注册"),
    EMAIL_SENT(10024, "Email已发送，若无法收到，请稍候再试"),
    EMAIL_NOT_SEND(10025, "请发送邮件验证"),
    EMAIL_AND_VERIFICATION_CODE_NOT_MATCH(10026, "验证码不正确，请重新输入"),
    TOKEN_EXPIRED(10027, "Token已过期，请重新登陆"),
    TOKEN_INVALID(10028, "Token无效"),
    FILE_PARSE_FAILED(10029, "文件解析失败，请检测格式是否正确"),
    SYSTEM_ERROR(20000, "系统错误");
    private Integer code;
    private String message;

    MallExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
