package com.shimanshu.security.service;

import com.shimanshu.security.entity.Seller;
import com.shimanshu.security.repository.PasswordResetTokenRepo;
import com.shimanshu.security.repository.SellerRepository;
import com.shimanshu.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private PasswordResetTokenRepo tokenRepository;

    public void saveUUIDTokenWithEmail(String email, String token){
        Token userAccessToken = new Token();
        userAccessToken.setToken(token);
        userAccessToken.setEmail(email);
        tokenRepository.save(userAccessToken);

    }

    public ResponseEntity validateToken(String activationToken){
        Token foundToken = tokenRepository.findBytoken(activationToken);
        System.out.println(foundToken);
        if (foundToken != null){
            Optional<Seller> seller = sellerRepository.findByEmail(foundToken.getEmail());
            seller.get().setActive(true);
            SellerRepository.save(seller);
            tokenRepository.delete(foundToken);
            return new ResponseEntity<String >("Account verify",null, HttpStatus.OK);
        }
        return new ResponseEntity<String>("Invalid Token",null,HttpStatus.UNAUTHORIZED);
    }

}
