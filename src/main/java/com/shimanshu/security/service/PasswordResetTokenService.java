package com.shimanshu.security.service;

import com.shimanshu.security.dto.ResetPasswordDto;
import com.shimanshu.security.entity.PasswordResetToken;
import com.shimanshu.security.entity.UserEntity;
import com.shimanshu.security.repository.PasswordResetTokenRepo;
import com.shimanshu.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class PasswordResetTokenService {
    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;

    @Autowired
    PasswordResetTokenRepo passwordResetTokenRepo;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MailSender mailSender;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public ResponseEntity<?> forgotPassword(String email) {
        if (userRepository.existsByEmail(email)) {
            return forgotPasswordUtility(email);
        } else {
            return new ResponseEntity<>("No user exists with this Email ID!", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> resetPassword(@Valid ResetPasswordDto resetPasswordDto) {

        UserEntity userEntity = userRepository.findById(passwordResetTokenRepo.findByUserId(resetPasswordDto.getToken())).get();
        PasswordResetTokenRepo passwordResetToken = (PasswordResetTokenRepo) passwordResetTokenRepo.findByToken(resetPasswordDto.getToken());
        if (passwordResetToken == null) {
            log.info("no token found");
            return new ResponseEntity<>("Invalid Token!", HttpStatus.BAD_REQUEST);
        } else {
            log.info("token found");
            ////isTokenExpired(passwordResetToken.getExpiresAt())
            if (true) {
                log.info("expired token");
                passwordResetTokenRepo.delete((PasswordResetToken) passwordResetToken);
                return new ResponseEntity<>("Token has been expired!", HttpStatus.BAD_REQUEST);
            } else {
                userEntity.setPassword(bCryptPasswordEncoder.encode(resetPasswordDto.getPassword()));
                log.info("password changed");
                userRepository.save(userEntity);
                passwordResetTokenRepo.delete((PasswordResetToken) passwordResetToken);

                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setSubject("Password Reset");
                mailMessage.setText("ALERT!, Your account password has been reset, If it was not you contact Admin asap.\nStay Safe, Thanks.");
                mailMessage.setTo(userEntity.getEmail());
                mailMessage.setFrom("sharda.kumari@tothenew.com");
                Date date = new Date();
                mailMessage.setSentDate(date);
                try {
                    log.info("mail sent");
                    mailSender.send(mailMessage);
                } catch (MailException e) {
                    log.info("Error sending mail");
                }
                return new ResponseEntity<>("Password Changed and deleted token", HttpStatus.OK);
            }
        }
    }

    private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);

        return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
    }

    public String generateToken(UserEntity userEntity) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                userEntity
        );
        passwordResetTokenRepo.save(passwordResetToken);
        return token;
    }

    public ResponseEntity<?> forgotPasswordUtility(String email) {
        UserEntity userEntity = userRepository.findUserByEmail(email);
        PasswordResetToken passwordResetToken = passwordResetTokenRepo.existsByUserId(userEntity.getId());
        if (passwordResetToken == null) {
            String token = generateToken(userEntity);
            log.info("password reset token generated");


            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Reset Password Link");
            mailMessage.setText("You seriously forgot your password\nIt's okay we got you...,\n Use below link to reset the password within 15 minutes."
                    +"\nhttp://localhost:8080/api/user/reset-password?token="+token
                    +"\nLink will expire after 15 minutes."
                    +"\nEnjoy.");
            mailMessage.setTo(userEntity.getEmail());
            mailMessage.setFrom("sharda.kumari@tothenew.com");
            Date date = new Date();
            mailMessage.setSentDate(date);
            try {
                mailSender.send(mailMessage);
                log.info("mail sent");
            } catch (MailException e) {
                log.info("Error sending mail");
            }
            return new ResponseEntity<>("Generated new Password Reset Token, sending to your mailbox", HttpStatus.OK);
        } else {
            passwordResetTokenRepo.deleteById(passwordResetToken.getId());
            log.info("deleted password reset token");
            String token = generateToken(userEntity);
            log.info("password reset token generated");


            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Reset Password Link");
            mailMessage.setText("You seriously forgot your password\nIt's okay we got you...,\n Use below link to reset the password within 15 minutes."
                    +"\nhttp://localhost:8080/api/user/reset-password?token="+token
                    +"\nLink will expire after 15 minutes."
                    +"\nEnjoy.");
            mailMessage.setTo(userEntity.getEmail());
            mailMessage.setFrom("sharda.kumari@tothenew.com");
            Date date = new Date();
            mailMessage.setSentDate(date);
            try {
                mailSender.send(mailMessage);
                log.info("mail sent");
            } catch (MailException e) {
                log.info("Error sending mail");
            }

            return new ResponseEntity<>("Existing Password Reset Token deleted and created new one\n" +
                    "check your mailbox.", HttpStatus.OK);
        }
    }
}
