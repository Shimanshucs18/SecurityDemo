package com.shimanshu.security.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URL;


@Service
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailSenderService {

    private JavaMailSender javaMailSender;
    private String toEmail;
    private String subject;
    private String message;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }
    @Async
    public void sendEmail(String email, String hello_seller, String url) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(hello_seller);
        mailMessage.setText(url);
        mailMessage.setFrom("shimanshu.sharma@tothenew.com");
        javaMailSender.send(mailMessage);

    }
}
