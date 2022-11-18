package com.shimanshu.security.dto;

public class RegisterDto {
    private String email;
    private String password;
    private Integer phoneNumber;
    private String firstName;
    private String lastName;

    public RegisterDto(String email, String password, Integer phoneNumber,String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastNameName(String lastNameName) {
        this.lastName = lastNameName;
    }

}
