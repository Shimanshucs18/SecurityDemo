package com.shimanshu.security.repository;

import com.shimanshu.security.controller.TokenDelete;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenDeleteRepository extends JpaRepository<TokenDelete,Long> {

    TokenDelete findByToken(String token);

}
