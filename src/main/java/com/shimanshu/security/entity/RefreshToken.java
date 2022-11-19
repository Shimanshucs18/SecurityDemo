package com.shimanshu.security.entity;

import com.shimanshu.security.service.Token;

import javax.persistence.*;

public class RefreshToken {

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

    public static void set(Token token) {
    }

    public static Token get() {
        return null;
    }
}
