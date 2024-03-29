package com.aston.hw2.service.impl;

import com.aston.hw2.dao.UserDao;
import com.aston.hw2.model.User;
import com.aston.hw2.model.dto.UserLoginDto;
import com.aston.hw2.service.AuthService;
import com.aston.hw2.util.CryptoTool;

import java.util.Optional;

public class AuthServiceImpl implements AuthService {
    private final UserDao userDao;

    public AuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getAuthenticatedUser(UserLoginDto userLoginDto) {
        Optional<User> userOpt = userDao.findByEmail(userLoginDto.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (CryptoTool.isCorrectPwd(userLoginDto.getPassword(), user.getPassword())) {
                return user;
            }
        }
        return null;
    }
}
