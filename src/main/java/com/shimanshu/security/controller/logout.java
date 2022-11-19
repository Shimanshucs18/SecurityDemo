package com.shimanshu.security.controller;

import com.shimanshu.security.entity.AccessToken;
import com.shimanshu.security.entity.RefreshToken;
import com.shimanshu.security.repository.AccessTokenRepository;
import com.shimanshu.security.service.Token;
import com.shimanshu.security.service.TokenRepository;
import org.apache.catalina.Session;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping(value ="/user")
public class logout {
    @Autowired
    AccessTokenRepository accessTokenRepository;

    @GetMapping("logout")
    public String logout(HttpServletRequest request) throws IOException {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken)&&bearerToken.startsWith("Bearer")){
            String tokenValue = bearerToken.substring(7,bearerToken.length());
            System.out.println(tokenValue);

            if(accessTokenRepository.existsByToken(tokenValue)){

                AccessToken accessToken = accessTokenRepository.findByToken(tokenValue);
                accessTokenRepository.delete(accessToken);
            }
        }
        return "Logged out Successfully";
    }
}
