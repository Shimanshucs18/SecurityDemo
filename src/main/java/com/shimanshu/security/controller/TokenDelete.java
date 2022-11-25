package com.shimanshu.security.controller;

import com.shimanshu.security.entity.UserEntity;

import javax.persistence.*;

@Entity
public class TokenDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)

    private String token;

    @Column(nullable = false)
    private String email;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public void setToken(String token) {
    }

    public void setUserEntity(Object user) {
    }

    public void setEmail(String email) {
    }
}
