package com.jjzhong.mall.common;

import com.jjzhong.mall.exception.MallException;
import com.jjzhong.mall.exception.MallExceptionEnum;
import io.lettuce.core.internal.LettuceSets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 常量
 */
@Component
public class Constant {
    /** 密码加密盐值 */
    public static final String SALT = "-j9O+GqYQ3%6";
    public static final String MALL_USER = "mall_user";
    public static final String EMAIL_SUBJECT = "您的验证码";
    /** jwt对称加密密钥（对称加密） */
    public static final String JWT_KEY = "MyMall";
    public static final String JWT_TOKEN = "jwt_token";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_ROLE = "user_role";
    public static final Integer VERIFICATION_CODE_LENGTH = 5;
    public static final Long EXPIRE_TIME = 60 * 1000 * 60 * 24 * 7L; // 单位为毫秒
    public static final Integer IMAGE_SIZE = 50;
    public static String UPLOAD_SCHEME;
    @Value("${mall.file.upload.schema}")
    public void setUploadScheme(String uploadScheme) {
        UPLOAD_SCHEME = uploadScheme;
    }

    public static String UPLOAD_HOST;
    @Value("${mall.file.upload.host}")
    public void setUploadHost(String uploadHost) {
        UPLOAD_HOST = uploadHost;
    }

    public static Integer UPLOAD_PORT;
    @Value("${mall.file.upload.port}")
    public void setUploadPort(Integer uploadPort) {
        UPLOAD_PORT = uploadPort;
    }

    public static String EMAIL_FROM;
    @Value("${spring.mail.username}")
    public void setEmailFrom(String emailFrom) {
        EMAIL_FROM = emailFrom;
    }

    public static String FILE_UPLOAD_DIR;

    @Value("${mall.file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir;
    }

    public static String FILE_UPLOAD_IMAGE_CONTEXT;

    @Value("${mall.file.upload.image.context}")
    public void setFileUploadImageContext(String fileUploadImageContext) {
        FILE_UPLOAD_IMAGE_CONTEXT = fileUploadImageContext;
    }

    public static String FILE_UPLOAD_FILE_CONTEXT;
    @Value("${mall.file.upload.file.context}")
    public void setFileUploadFileContext(String fileUploadFileContext) {
        FILE_UPLOAD_FILE_CONTEXT = fileUploadFileContext;
    }

    public static String ORDER_PAY_HOST;

    @Value("${mall.order.pay.host}")
    public void setOrderPayHost(String orderPayHost) {
        ORDER_PAY_HOST = orderPayHost;
    }

    public interface Role {
        int CUSTOMER = 1;
        int ADMIN = 2;
    }
    public interface ProductListOrderBy {
        Set<String> PRICE_ORDER_ENUM = LettuceSets.newHashSet("price desc", "price asc");
    }

    public interface ProductSellStatus {
        int NOT_SELL = 0;
        int SELL = 1;
    }

    public interface CartSelectStatus {
        int UN_SELECTED = 0;
        int SELECTED = 1;
    }

    public enum OrderStatusEnum {
        CANCELED(0, "用户已取消"),
        NOT_PAY(10, "未付款"),
        PAID(20, "已付款"),
        DELIVERED(30, "已发货"),
        FINISHED(40, "交易完成");
        private Integer code;
        private String name;

        OrderStatusEnum(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public static OrderStatusEnum codeOf(Integer code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode().equals(code)) {
                    return orderStatusEnum;
                }
            }
            throw new MallException(MallExceptionEnum.ORDER_STATUS_INCORRECT);
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
