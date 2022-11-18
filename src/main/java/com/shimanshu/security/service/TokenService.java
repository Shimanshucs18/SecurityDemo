package com.shimanshu.security.service;


import com.shimanshu.security.controller.SellerRepository;
import com.shimanshu.security.entity.Seller;
import com.shimanshu.security.repository.UserRepository;
import org.apache.catalina.Session;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TokenService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerRepository sellerRepository;

    public void saveUUIDTokenWithEmail(String email, String token) throws IOException {
        Token userAccessToken = new Token();
        userAccessToken.setToken(token);
        userAccessToken.setEmail(email);
        Store tokenRepository = null;
        tokenRepository.save((Session) userAccessToken);
    }
    public ResponseEntity validateToken(String activationToken){
        antlr.Token foundToken = tokenRepository.findBytoken(activationToken);

        if(foundToken!=null){
            Seller seller;
            seller = sellerRepository.findByemail(foundToken.getEmail);
            seller.setActive(true);
            sellerRepository.save(seller);
            tokenRepository.delete(foundToken);
            return new ResponseEntity<String>("Account Verified",null, HttpStatus.OK);
        }
        return new ResponseEntity<String>("Invalid Token!", null,HttpStatus.UNAUTHORIZED);
    }



}
