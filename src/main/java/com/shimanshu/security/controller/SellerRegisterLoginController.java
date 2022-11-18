package com.shimanshu.security.controller;


import com.shimanshu.security.dto.AuthResponseDto;
import com.shimanshu.security.dto.LoginDto;
import com.shimanshu.security.dto.RegisterDto;
import com.shimanshu.security.entity.Role;
import com.shimanshu.security.entity.Seller;
import com.shimanshu.security.repository.RoleRepository;
import com.shimanshu.security.repository.UserRepository;
import com.shimanshu.security.security.JWTGenerator;
import com.shimanshu.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/seller")
public class SellerRegisterLoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncode;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private EmailSenderService emailSenderService;
    private TokenService service;

    //register
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) throws IOException {
        if(userRepository.existsByemail(registerDto.getEmail())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }
        Seller user =  new Seller();
//        UserEntity user = new UserEntity();
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncode.encode((registerDto.getPassword())));

        Role role =  roleRepository.findByAuthority("SELLER").get();
        user.setRole(Collections.singletonList(role));

        sellerRepository.save(user);
        String ID = UUID.randomUUID().toString();
        String URL = "http://localhost:8080/seller/activate?token="+ID;
        service.saveUUIDTokenWithEmail(registerDto.getEmail(),ID);

        emailSenderService.sendEmail(registerDto.getEmail(),"HELLO SELLER ",URL);
        return new ResponseEntity<>("User Registered success!",HttpStatus.OK);
    }
    //Login
  @PostMapping("login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new usernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String token = jwtGenerator.generateToken(authentication);

      return new ResponseEntity<>(new AuthResponseDto(token),HttpStatus.OK);
    }

    boolean activateUser(@RequestParam("token") String activationToken){
        JWTGenerator service = null;
        return service.validateToken(activationToken);
    }


}
