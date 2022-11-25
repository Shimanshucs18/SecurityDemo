package com.shimanshu.security.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordDto {

    @NotBlank(message = "Access token cannot be blank")
    private String accessToken;

    private String password;

    @NotBlank(message = "Confirm Password should be same to Password")
    private String confirmPassword;
}
