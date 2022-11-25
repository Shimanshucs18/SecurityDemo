package com.shimanshu.security.service;

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
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    @Autowired
    JavaMailSender javaMailSender;

    public String forgotPassword(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent()) {
            Optional<ForgotPasswordToken> forgotPasswordToken = forgotPasswordRespository.getTokenByUserId(userEntity.get().getId());

            /* If forget password token already exist*/
            if (forgotPasswordToken.isPresent()) {
                forgotPasswordRespository.deleteByTokenId(forgotPasswordToken.get().getId());
            }
            /* Generate new ForgetToken */
            ForgotPasswordToken forgetPassword = forgerPassGenerate(userEntity.get());
            forgotPasswordRespository.save(forgetPassword);

            /* Send Mail with rest password Link */
            String URL = "To reset you password click the link below within 15 minutes \n" + "http://127.0.0.1:9091/api/auth/forget-password/" + forgetPassword.getToken();
            emailService.sendSimpleMail(userEntity.get().getEmail(), "Reset Password", URL);
            return "Reset Password Token Generated || Please check your email || Verify within 15 Minutes";
        } else {
            return "User with Email : " + "not found";
        }
    }

    public ForgotPasswordToken forgerPassGenerate(UserEntity userEntity) {
        String token = UUID.randomUUID().toString();
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setToken(token);
        forgotPasswordToken.setUserEntity(userEntity);
        forgotPasswordToken.setCreatedAt(LocalDateTime.now());
        forgotPasswordToken.setExpireAt(LocalDateTime.now());
        return forgotPasswordToken;
    }

    public boolean verifyToken(LocalDateTime localDateTime) {

        Duration timeDiff = Duration.between(localDateTime, LocalDateTime.now());
        return timeDiff.toMinutes() >= SecurityConstants.RESET_PASS_EXPIRE_MINUTES;
    }

    @Transactional
    public ResponseEntity<String> resetUserPassword(ResetPasswordDto resetPasswordDto) {
        /* Add validation here */
        UserEntity userEntity = userRepository.findByEmail(resetPasswordDto.getEmail()).get();
        Optional<ForgotPasswordToken> forgotPasswordToken = forgotPasswordRespository.findByToken(resetPasswordDto.getToken());

        if (forgotPasswordToken.isPresent()) {
            if (verifyToken(forgotPasswordToken.get().getExpireAt())) {
                forgotPasswordRespository.deleteByTokenId(forgotPasswordToken.get().getId());
                return new ResponseEntity<>("Token has been expired", HttpStatus.BAD_REQUEST);
            } else {
                userEntity.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
                userRepository.save(userEntity);

                forgotPasswordRespository.deleteByTokenId(forgotPasswordToken.get().getId());
                /* Send Email Here */
                return new ResponseEntity<>("Password changed", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("Reset Token not Found", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity confirmById1(Long id) {
        if (userRepository.existsById(id)) {
            System.out.println("users exists");

            if (userRepository.isUserActive(id)) {
                return ResponseEntity.ok("users created");
            } else {
                Optional<UserEntity> userEntity = userRepository.findById(id);
                userRepository.save(userEntity.get());

                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setSubject("Account Activated by Admin");
                mailMessage.setText("Your account has been activated by Admin:)");
                mailMessage.setTo(userEntity.get().getEmail());

                try {
                    javaMailSender.send(mailMessage);
                } catch (MailException e) {
                    System.out.println("error sending mail");
                }
                System.out.println("User activated!!");
            }
        } else {

            return ResponseEntity.badRequest().body(String.format("No user exists with this user id: %s", id));
        }
        return ResponseEntity.ok().body(String.format("User activated with this user id: %s", id));
    }

    public ResponseEntity<?> removeById(Long id) {
        if (userRepository.existsById(id)) {
            System.out.println("customer exists");

            Optional<UserEntity> userEntity;
            if (!userRepository.isUserActive(id)) {
                return ResponseEntity.ok("already de activated user");

            } else {
                userEntity = userRepository.findById(id);
                userEntity.get().setActive(false);
                userRepository.save(userEntity.get());
            }
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Account deactivated by Admin | please contact to the admin");
            mailMessage.setText("Your account has been deactivated by Admin:)");
            mailMessage.setTo(userEntity.get().getEmail());
            mailMessage.setFrom("shimanshu.sharma@tothenew.com");
            try {
                javaMailSender.send(mailMessage);
            } catch (MailException e) {
                System.out.println("error sending mail");
            }
            System.out.println("User activated!!");
        } else {

            return ResponseEntity.badRequest().body(String.format("No user exists with this user id: %s", id));
        }
        return ResponseEntity.ok().body(String.format("User deactivated with this user id: %s", id));
    }

    public ResponseEntity<String> resultUserPassword(ResetPasswordDto resetPasswordDto) {
        UserEntity userEntity = userRepository.findByEmail(resetPasswordDto.getEmail()).get();
        Optional<ForgotPasswordToken> forgotPasswordToken = forgotPasswordRespository.findByToken(resetPasswordDto.getToken());

        if (forgotPasswordToken.isPresent()) {
            if (verifyToken(forgotPasswordToken.get().getExpireAt())) {
                forgotPasswordRespository.deleteByTokenId(forgotPasswordToken.get().getId());
                return new ResponseEntity<>("Token has been Expired", HttpStatus.BAD_REQUEST);
            } else {
                userEntity.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
                userRepository.save(userEntity);
                forgotPasswordRespository.deleteByTokenId(forgotPasswordToken.get().getId());

                return new ResponseEntity<>("Password changed", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("Reset token not found", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> confirmById(Long id) {
        if (userRepository.existsById(id)) {
            System.out.println("User Exists");

            if (userRepository.isUserActive(id)) {
                return ResponseEntity.ok("User Created");
            } else {
                Optional<UserEntity> userEntity = userRepository.findById(id);
                userEntity.get().setIsActive(false);
                userRepository.save(userEntity.get());

                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setSubject("Account Activated By Admin");
                mailMessage.setText("Your Account has been Activated by Admin:)");
                mailMessage.setTo(userEntity.get().getEmail());
                mailMessage.setFrom("shimanshu.sharma@tothenew.com");
                try {
                    javaMailSender.send(mailMessage);
                } catch (MailException e) {
                    System.out.println("Error Sending Mail");
                }
                System.out.println("User Activated!!");
            }
        } else {
            return ResponseEntity.badRequest().body(String.format("No User exist with this User id: %s", id));
        }
        return ResponseEntity.ok().body(String.format("User deactivated with this User id: %s", id));
    }
}




