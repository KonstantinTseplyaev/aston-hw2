package com.aston.hw2.service;

import com.aston.hw2.model.User;
import com.aston.hw2.model.dto.UserLoginDto;

public interface AuthService {
    User getAuthenticatedUser(UserLoginDto userLoginDto);
}
