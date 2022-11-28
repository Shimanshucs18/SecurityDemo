package com.shimanshu.security.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Email(flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email should be Unique and Valid")
    @NotBlank(message = "Email can't be Empty")
    private String email;


    @NotBlank(message = "Password can't be Empty")
    @Size(message = "Enter correct Password or else after 3rd Attempt , Account there will be Locked!!")
    private String password;

    Admin(){

    }

    public Admin(String email,String password){
        this.email=email;
        this.password=password;
    }

    public Long id(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

}
