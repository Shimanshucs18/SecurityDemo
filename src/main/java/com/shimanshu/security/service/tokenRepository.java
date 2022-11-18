package com.shimanshu.security.service;

import antlr.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface tokenRepository extends JpaRepository<Token,Integer> {
    static Token findBytoken(String activationToken) {
        return null;
    }
}
