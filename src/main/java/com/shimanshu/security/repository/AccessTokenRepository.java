package com.shimanshu.security.repository;

import com.shimanshu.security.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepository extends JpaRepository<AccessToken,Long> {

    AccessToken findByToken(String token);
    boolean existsByToken(String token);

}
