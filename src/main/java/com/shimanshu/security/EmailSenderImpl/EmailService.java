package com.shimanshu.security.EmailSenderImpl;

import com.shimanshu.security.entity.EmailDetails;

public interface EmailService {

    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}
