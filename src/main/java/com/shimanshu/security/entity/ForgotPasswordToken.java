package com.shimanshu.security.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "forget_password_token")
public class ForgotPasswordToken {
    @Id
    @SequenceGenerator(name = "forget_pass_sequence",sequenceName = "forget_pass_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "forget_pass_sequence")
    private Long id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expireAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public ForgotPasswordToken(Long id, String token, LocalDateTime CreatedAt,LocalDateTime expireAt, UserEntity userEntity){
        this.id = id;
        this.token = token;
        this.createdAt = CreatedAt;
        this.expireAt = expireAt;
        this.userEntity = userEntity;
    }
}
