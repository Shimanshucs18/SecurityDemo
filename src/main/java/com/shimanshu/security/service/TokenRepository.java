package com.shimanshu.security.service;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Integer> {
    Optional<Token> findBytoken(String activationToken);

    @Modifying
    void deleteByToken(String token);

}