package com.shimanshu.security.service;

import com.shimanshu.security.entity.UserEntity;
import lombok.Data;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;

@Entity
@Data
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String token;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userID")
    private UserEntity user;
}
