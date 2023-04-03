package com.jjzhong.mall.service.impl;

import com.jjzhong.mall.common.Constant;
import com.jjzhong.mall.exception.MallException;
import com.jjzhong.mall.exception.MallExceptionEnum;
import com.jjzhong.mall.model.dao.UserMapper;
import com.jjzhong.mall.model.pojo.User;
import com.jjzhong.mall.service.EmailService;
import com.jjzhong.mall.util.EmailUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 邮件服务
 */
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 发送简单的（只包含文本的） Email
     * @param from 来源 Email
     * @param to 目的 Email
     * @param subject 主题
     * @param text 内容
     */
    @Override
    public void sendSimpleEmailMessage(String from, String to, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        mailSender.send(simpleMailMessage);
    }

    /**
     * 发送验证码邮件
     * @param emailAddress 邮件地址
     */
    @Override
    public void sendVerifyCodeEmail(String emailAddress) {
        EmailUtils.isValidEmailAddress(emailAddress);
        String verificationCode = EmailUtils.generateVerificationCode(Constant.VERIFICATION_CODE_LENGTH);
        saveVerifyCodeToRedis(emailAddress, verificationCode);
        sendSimpleEmailMessage(Constant.EMAIL_FROM, emailAddress, Constant.EMAIL_SUBJECT, verificationCode);
    }

    /**
     * 查询邮件地址是否被注册
     * @param emailAddress 邮件地址
     */
    @Override
    public void checkEmailRegistered(String emailAddress) {
        User user = userMapper.selectByEmailAddress(emailAddress);
        if (user != null) {
            throw new MallException(MallExceptionEnum.EMAIL_REGISTERED);
        }
    }

    /**
     * 检查验证码与邮箱是否匹配
     * @param request 请求
     * @return 验证结果
     */
    @Override
    public void checkIsMatch(String emailAddress, String verificationCode) {
        RBucket<String> bucket = redissonClient.getBucket(emailAddress);
        if (!bucket.isExists()) {
            throw new MallException(MallExceptionEnum.EMAIL_NOT_SEND);
        }
        if (!bucket.get().equals(verificationCode)) {
            throw new MallException(MallExceptionEnum.EMAIL_AND_VERIFICATION_CODE_NOT_MATCH);
        }
    }

    /**
     * 将验证码保存在 redis 中
     * @param emailAddress 邮件地址
     * @param verificationCode 验证码
     */
    private void saveVerifyCodeToRedis(String emailAddress, String verificationCode) {
        RBucket<String> bucket = redissonClient.getBucket(emailAddress);
        if (!bucket.isExists()) {
            bucket.set(verificationCode, 60, TimeUnit.SECONDS);
        } else {
            throw new MallException(MallExceptionEnum.EMAIL_SENT);
        }
    }
}
