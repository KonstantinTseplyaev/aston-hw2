package com.aston.hw2.service.impl;

import com.aston.hw2.model.User;
import com.aston.hw2.model.dto.UserRegistrationDto;
import com.aston.hw2.dao.UserDao;
import com.aston.hw2.service.UserService;

import java.util.Optional;

import static com.aston.hw2.util.MapperUtil.toUserFromUserRegistrationDto;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean addUser(UserRegistrationDto user) {
        Optional<User> userOpt = userDao.findByEmail(user.getEmail());
        if (userOpt.isPresent()) {
            return false;
        }
        User newUser = toUserFromUserRegistrationDto(user);
        return userDao.addUser(newUser);
    }
}
