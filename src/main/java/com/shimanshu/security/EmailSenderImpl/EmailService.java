package com.shimanshu.security.EmailSenderImpl;

import com.shimanshu.security.entity.EmailDetails;

public interface EmailService {

    // To send a simple email
    String sendSimpleMail(String email,String subject,String body);

    // To send an email with attachment

}
