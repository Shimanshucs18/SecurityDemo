package com.shimanshu.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDao {
    private String email;
    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;



}
