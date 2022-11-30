package com.shimanshu.security.dto;

import com.shimanshu.security.CustomValidation.PasswordMatchesForResetPasswordRequest;
import com.shimanshu.security.CustomValidation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatchesForResetPasswordRequest
public class ResetPasswordDto {
    @NotBlank(message = "Password Reset Token cannot be blank")
    private String token;

    @ValidPassword
    private String password;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 16, message = "Password should be same to Password")
    private String confirmPassword;
}
