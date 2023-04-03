package com.jjzhong.mall.exception;

import com.jjzhong.mall.common.CommonResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Set;

/**
 * 统一异常处理类
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 自定义异常的处理方法
     * @param e 自定义异常
     * @return 统一响应
     */
    @ExceptionHandler(MallException.class)
    public CommonResponse handleMallException(MallException e) {
        logger.info("MallException: ", e);
        return CommonResponse.error(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常的处理方法
     * @param e 异常
     * @return 统一响应
     */
    @ExceptionHandler(BindException.class)
    public CommonResponse handleMethodArgumentNotValidException(BindException e) {
        logger.info("MethodArgumentNotValidException: ", e);
        return handleBindingResult(e.getBindingResult());
    }

    /**
     * 缺少传递的参数异常的处理方法
     * @param e 异常
     * @return 统一响应
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public CommonResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.info("MissingServletRequestParameterException: ", e);
        return CommonResponse.error(MallExceptionEnum.REQUEST_PARAM_ERROR);
    }

    /**
     * 参数读取异常的处理方法
     * @param e 异常
     * @return 统一响应
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.info("HttpMessageNotReadableException: ", e);
        return CommonResponse.error(MallExceptionEnum.REQUEST_PARAM_ERROR);
    }

    /**
     * 参数未通过校验异常的处理方法，会返回相应字段的错误提示，例如：
     * {
     *   "status": 10006,
     *   "msg": "参数错误: 邮箱格式错误",
     *   "data": null
     * }
     * @param e 异常
     * @return 清晰的统一相应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResponse handleConstraintViolationException(ConstraintViolationException e) {
        logger.info("ConstraintViolationException: ", e);
        return handleConstraintViolation(e.getConstraintViolations());
    }

    /**
     * 其他异常
     * @param e 异常
     * @return 统一响应
     */
    @ExceptionHandler(Exception.class)
    public CommonResponse handleException(Exception e) {
        logger.error("Exception: ", e);
        return CommonResponse.error(MallExceptionEnum.SYSTEM_ERROR);
    }

    /**
     * 将参数错误异常包装成友好的提示消息
     * @param result BindException 中绑定的错误结果
     * @return 清晰友好的提示字符串，例如："参数错误: 邮箱格式错误, 验证码不能为空"
     */
    private CommonResponse handleBindingResult(BindingResult result) {
        StringBuilder str = new StringBuilder(MallExceptionEnum.REQUEST_PARAM_ERROR.getMessage()).append(": ");
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            for (int i = 0; i < allErrors.size(); i ++) {
                 str.append(allErrors.get(i).getDefaultMessage()).append(i == allErrors.size() - 1 ? "" : ", ");
            }
        }
        return CommonResponse.error(MallExceptionEnum.REQUEST_PARAM_ERROR.getCode(), str.toString());
    }

    /**
     * 将参数错误异常包装成友好的提示消息
     * @param constraintViolations ConstraintViolationException 中的错误校验结果
     * @return 清晰友好的提示字符串，例如："参数错误: 邮箱格式错误, 验证码不能为空"
     */
    private CommonResponse handleConstraintViolation(Set<ConstraintViolation<?>> constraintViolations) {
        StringBuilder str = new StringBuilder(MallExceptionEnum.REQUEST_PARAM_ERROR.getMessage()).append(": ");
        String res = null;
        if (!constraintViolations.isEmpty()) {
            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                str.append(constraintViolation.getMessage()).append(", ");
            }
            res = str.substring(0, str.length() - 2);
        }
        return CommonResponse.error(MallExceptionEnum.REQUEST_PARAM_ERROR.getCode(), res);
    }
}
