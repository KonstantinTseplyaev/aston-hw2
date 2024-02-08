package com.aston.hw2.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRegistrationDto {
    private String name;
    private String email;
    private String password;
}
