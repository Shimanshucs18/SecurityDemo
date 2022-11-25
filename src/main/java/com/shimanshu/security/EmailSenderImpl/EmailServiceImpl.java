package com.shimanshu.security.EmailSenderImpl;

import com.shimanshu.security.entity.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;
//    Method 1
//    To send Simple Email
//    public String sendSimpleMail(EmailDetails details){
//        try {
//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//            mailMessage.setFrom(sender);
//            mailMessage.setTo(details.getRecipient());
//            mailMessage.setText(details.getMsgBody());
//            mailMessage.setSubject(details.getSubject());
//
//            javaMailSender.send(mailMessage);
//            return "Mail Sent Successfully...";
//        }
//        catch (Exception e){
//            return "Error while Sending Mail";
//        }
//    }

    public String sendSimpleMail(String email,String subject,String body){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(email);
            mailMessage.setText(body);
            mailMessage.setSubject(subject);

            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }
        catch (Exception e){
            return "Error while Sender Mail";
        }
    }

    @Override
    public String sendMailWithAttachment(EmailDetails emailDetails) {
        return null;
    }

    @Override
    public String sendSimpleMail(EmailDetails details) {
        return null;
    }


}
