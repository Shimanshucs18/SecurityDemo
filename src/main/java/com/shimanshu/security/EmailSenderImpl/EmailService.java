package com.shimanshu.security.EmailSenderImpl;

import com.shimanshu.security.entity.EmailDetails;

public interface EmailService {

    // To send a simple email
    String sendSimpleMail(String email,String subject,String body);

    String sendMailWithAttachment(EmailDetails emailDetails);

    String sendSimpleMail(EmailDetails details);

    // To send an email with attachment

}
