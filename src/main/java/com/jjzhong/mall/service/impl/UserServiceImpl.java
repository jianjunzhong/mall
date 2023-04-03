package com.jjzhong.mall.service.impl;

import com.jjzhong.mall.common.Constant;
import com.jjzhong.mall.exception.MallException;
import com.jjzhong.mall.exception.MallExceptionEnum;
import com.jjzhong.mall.model.dao.UserMapper;
import com.jjzhong.mall.model.pojo.User;
import com.jjzhong.mall.model.request.RegisterReq;
import com.jjzhong.mall.service.EmailService;
import com.jjzhong.mall.service.UserService;
import com.jjzhong.mall.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EmailService emailService;

    /**
     * 注册新用户
     * @param registerReq 注册请求
     */
    @Override
    public void register(RegisterReq registerReq) {
        User result = userMapper.selectByUserName(registerReq.getUserName());
        if (result != null) {
            throw new MallException(MallExceptionEnum.USER_NAME_EXISTED);
        }
        emailService.checkEmailRegistered(registerReq.getEmail());
        emailService.checkIsMatch(registerReq.getEmail(), registerReq.getVerificationCode());
        User user = new User();
        user.setUsername(registerReq.getUserName());
        user.setPassword(MD5Utils.getMD5Str(registerReq.getPassword()));
        user.setEmailAddress(registerReq.getEmail());
        int count = userMapper.insertSelective(user);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.INSERT_FAILED);
        }
    }

    /**
     * 普通登陆
     * @param userName 用户名
     * @param password 密码
     * @return 用户 POJO
     */
    @Override
    public User login(String userName, String password) {
        String md5Password = MD5Utils.getMD5Str(password);
        User user = userMapper.selectLogin(userName, md5Password);
        if (user == null) {
            throw new MallException(MallExceptionEnum.NOT_VALID_USER_NAME_OR_PASSWORD);
        }
        return user;
    }


    /**
     * 更新用户信息
     * @param user 用户
     */
    @Override
    public void updateInformation(User user) {
        int cnt = userMapper.updateByPrimaryKeySelective(user);
        if (cnt != 1) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    /**
     * 管理员登录
     * @param userName 用户名
     * @param password 密码
     * @return 用户 POJO
     */
    @Override
    public User adminLogin(String userName, String password) {
        User user = login(userName, password);
        if (!checkAdmin(user)) {
            throw new MallException(MallExceptionEnum.NEED_ADMIN);
        }
        return user;
    }

    /**
     * 校验是否为管理员
     * @param user 用户
     * @return
     */
    @Override
    public boolean checkAdmin(User user) {
        return user.getRole().equals(Constant.Role.ADMIN);
    }
}
