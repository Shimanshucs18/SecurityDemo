package com.shimanshu.security.repository;

import com.shimanshu.security.entity.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ForgotPasswordRespository extends JpaRepository<ForgotPasswordToken,Long> {
    @Query(value = "select * from forget_password_token where user_id =id",nativeQuery = true)
    Optional<ForgotPasswordToken> getTokenByUserId(@Param("id") Long id);

    Optional<ForgotPasswordToken> findByToken(String Token);

    @Modifying
    @Query(value = "DELETE from forget_password_token where id =:id",nativeQuery = true)
    public void deleteByTokenId(@Param("id") Long id);
}
