package com.shimanshu.security.service;

import com.shimanshu.security.EmailSenderImpl.EmailService;
import com.shimanshu.security.EmailSenderImpl.EmailServiceImpl;
import com.shimanshu.security.dto.ResetPasswordDto;
import com.shimanshu.security.entity.ForgotPasswordToken;
import com.shimanshu.security.entity.UserEntity;
import com.shimanshu.security.repository.ForgotPasswordRespository;
import com.shimanshu.security.repository.UserRepository;
import com.shimanshu.security.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserPasswordService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ForgotPasswordRespository forgotPasswordRespository;
    @Autowired
    EmailServiceImpl emailService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public String forgotPassword(String email){
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent()){
            Optional<ForgotPasswordToken> forgotPasswordToken = forgotPasswordRespository.getTokenByUserId(userEntity.get().getId());

            /* If forget password token already exist*/
            if (forgotPasswordToken.isPresent()){
                forgotPasswordRespository.deleteByTokenId(forgotPasswordToken.get().getId());
            }
            /* Generate new ForgetToken */
            ForgotPasswordToken forgetPassword = forgerPassGenerate(userEntity.get());
            forgotPasswordRespository.save(forgetPassword);

            /* Send Mail with rest password Link */
            String URL="To reset you password click the link below within 15 minutes \n"
                    + "http://127.0.0.1:9091/api/auth/forget-password/"
                    +forgetPassword.getToken();
            emailService.sendSimpleMail(userEntity.get().getEmail(),"Reset Password",URL);
            return "Reset Password Token Generated || Please check your email || Verify within 15 Minutes";
        }else{
            return "User with Email : " + "not found";
        }
    }

    public ForgotPasswordToken forgerPassGenerate(UserEntity userEntity){
        String token = UUID.randomUUID().toString();
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setToken(token);
        forgotPasswordToken.setUserEntity(userEntity);
        forgotPasswordToken.setCreatedAt(LocalDateTime.now());
        forgotPasswordToken.setExpireAt(LocalDateTime.now());
        return forgotPasswordToken;
    }
    public boolean verifyToken(LocalDateTime localDateTime){

        Duration timeDiff = Duration.between(localDateTime,LocalDateTime.now());
        return timeDiff.toMinutes() >= SecurityConstants.RESET_PASS_EXPIRE_MINUTES;
    }

    @Transactional
    public ResponseEntity<String> resetUserPassword(ResetPasswordDto resetPasswordDto){
        /* Add validation here */
        UserEntity userEntity =userRepository.findByEmail(resetPasswordDto.getEmail()).get();
        Optional<ForgotPasswordToken> forgotPasswordToken = forgotPasswordRespository.findByToken(resetPasswordDto.getToken());

        if (forgotPasswordToken.isPresent()){
            if (verifyToken(forgotPasswordToken.get().getExpireAt())){
                forgotPasswordRespository.deleteByTokenId(forgotPasswordToken.get().getId());
                return new ResponseEntity<>("Token has been expired", HttpStatus.BAD_REQUEST);
            }else {
                 userEntity.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
                userRepository.save(userEntity);

                forgotPasswordRespository.deleteByTokenId(forgotPasswordToken.get().getId());
                /* Send Email Here */
                return new ResponseEntity<>("Password changed",HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<>("Reset Token not Found", HttpStatus.BAD_REQUEST);
        }
    }

}
