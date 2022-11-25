package com.shimanshu.security.repository;

import com.shimanshu.security.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken,Long> {

    Optional<AccessToken> findByToken(String token);
    boolean existsByToken(String token);

    void deleteByToken(String token);

//    Optional<AccessToken> findByToken(String token);


}
