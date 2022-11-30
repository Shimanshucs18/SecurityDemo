package com.shimanshu.security.repository;

import com.shimanshu.security.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, Long> {
    @Query(value = "SELECT * FROM password_reset_token a WHERE a.user_id =:id", nativeQuery = true)
    PasswordResetToken existsByUserId(@Param("id") Long id);

    @Query(value = "SELECT * FROM password_reset_token a WHERE a.token =:token", nativeQuery = true)
    PasswordResetToken findByToken(@Param("token") String token);

    @Query(value = "SELECT a.user_id FROM password_reset_token a WHERE a.token =:token", nativeQuery = true)
    Long findByUserId(@Param("token") String token);

}
