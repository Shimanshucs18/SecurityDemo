package com.shimanshu.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordDto{
    private String email;
    private String password;
    private String token;
}
