package com.shimanshu.security.dto;

import com.shimanshu.security.CustomValidation.PasswordMatchesForCustomer;
import com.shimanshu.security.CustomValidation.ValidPassword;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
@PasswordMatchesForCustomer
public class SignupCustomerDao {
    private String firstName;
    private String lastName;

    @Pattern(regexp="(^$|[0-9]{10})", message = "Please Phone number is minimum 10 digits")
    @NotBlank(message = "Phone number cannot be empty")
    private String contact;

    @Email(flags = Pattern.Flag.CASE_INSENSITIVE, message = "Please Email write unique and valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @ValidPassword
    private String password;

    @NotBlank(message = "Please Password can't be empty")
    @Size(min = 8, max = 16, message = "Password should be same to Password")
    private String confirmPassword;

}
