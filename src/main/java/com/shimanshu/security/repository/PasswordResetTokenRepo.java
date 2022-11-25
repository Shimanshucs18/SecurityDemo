package com.shimanshu.security.repository;

import com.shimanshu.security.service.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepo extends JpaRepository<Token,Long> {
    void deleteByToken(String token);

    Token findBytoken(String activationToken);
}
