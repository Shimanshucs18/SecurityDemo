package com.shimanshu.security.service;


import com.shimanshu.security.entity.UserEntity;
import com.shimanshu.security.repository.SellerRepository;
import com.shimanshu.security.entity.Seller;
import com.shimanshu.security.repository.UserRepository;
import org.apache.catalina.Session;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TokenService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    TokenRepository tokenRepository;

    public void saveUUIDTokenWithEmail(String email, String token) throws IOException {
        Token userAccessToken = new Token();
        userAccessToken.setToken(token);
        userAccessToken.setEmail(email);
        tokenRepository.save(userAccessToken);
    }
    public ResponseEntity validateToken(String activationToken){
        Token foundToken = tokenRepository.findBytoken(activationToken).get();
        
//        findByEmail(foundToken.getEmail);

        if(foundToken!=null){
            UserEntity user;
            user = userRepository.findByEmail(foundToken.getEmail()).get();
            user.setIsActive(true);
            userRepository.save(user);
            tokenRepository.delete(foundToken);
            return new ResponseEntity<String>("Account Verified",null, HttpStatus.OK);
        }
        return new ResponseEntity<String>("Invalid Token!", null,HttpStatus.UNAUTHORIZED);
    }



}
