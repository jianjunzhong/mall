package com.jjzhong.mall.service;

import com.jjzhong.mall.exception.MallException;
import com.jjzhong.mall.model.pojo.User;
import com.jjzhong.mall.model.request.RegisterReq;
import org.springframework.stereotype.Service;

public interface UserService {
    void register(RegisterReq registerReq) throws MallException;

    User login(String userName, String password) throws MallException;

    void updateInformation(User user) throws MallException;

    User adminLogin(String userName, String password) throws MallException;

    boolean checkAdmin(User user);
}
