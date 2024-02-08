package com.aston.hw2.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginDto {
    private String email;
    private String password;
}
