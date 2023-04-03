package com.jjzhong.mall.util;

import com.jjzhong.mall.exception.MallException;
import com.jjzhong.mall.exception.MallExceptionEnum;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * 电子邮件工具类
 */
public class EmailUtils {
    /**
     * 生成随机验证码
     * @param num 验证码长度
     * @return 验证码
     */
    public static String generateVerificationCode(Integer num) {
        return RandomStringUtils.randomAlphanumeric(num);
    }

    /**
     * 校验是否是合法的电子邮件地址
     * @param email 电子邮件地址
     */
    public static void isValidEmailAddress(String email) {
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
        } catch (AddressException e) {
            throw new MallException(MallExceptionEnum.INCORRECT_EMAIL_ADDRESS);
        }
    }
}
