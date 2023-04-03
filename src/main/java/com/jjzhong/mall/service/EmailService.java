package com.jjzhong.mall.service;

public interface EmailService {

    void sendSimpleEmailMessage(String from, String to, String subject, String text);

    void sendVerifyCodeEmail(String email);

    void checkEmailRegistered(String emailAddress);

    void checkIsMatch(String emailAddress, String verificationCode);
}
