package com.shimanshu.security.controller;

import com.shimanshu.security.dto.AuthResponseDto;
import com.shimanshu.security.dto.LoginDto;
import com.shimanshu.security.dto.RegisterDto;
import com.shimanshu.security.entity.AccessToken;
import com.shimanshu.security.entity.Customer;
import com.shimanshu.security.entity.Role;
import com.shimanshu.security.entity.UserEntity;
import com.shimanshu.security.repository.AccessTokenRepository;
import com.shimanshu.security.repository.CustomerRepository;
import com.shimanshu.security.repository.RoleRepository;
import com.shimanshu.security.repository.UserRepository;
import com.shimanshu.security.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping(value = "/api/auth")
public class PublicController {

    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncode;

    private JWTGenerator jwtGenerator;

    private AccessTokenRepository accessTokenRepository;

    @Autowired
    public PublicController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncode, JWTGenerator jwtGenerator, AccessTokenRepository accessTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncode = passwordEncode;
        this.jwtGenerator = jwtGenerator;
        this.accessTokenRepository = accessTokenRepository;
    }


    @PostMapping("login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        UserEntity user = userRepository.findByEmail(loginDto.getEmail()).get();
        AccessToken accessToken=new AccessToken();
        accessToken.setToken(token);
        accessToken.setEmail(loginDto.getEmail());
        accessToken.setUserEntity(user);

        accessTokenRepository.save(accessToken);

        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);

    }

    @GetMapping("hello")
    public String display() {
        return "hello";
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity<>("Email is already taken", HttpStatus.BAD_REQUEST);
        }
        Customer customer =new Customer();
        customer.setContact(registerDto.getPhoneNumber());
        UserEntity user = new UserEntity();
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncode.encode(registerDto.getPassword()));
        user.setCustomer(customer);
        System.out.println(user);
        Role roles = roleRepository.findByAuthority("CUSTOMER").get();
        user.setRoles(Collections.singletonList(roles));

        userRepository.save(user);

        return new ResponseEntity<>("Customer Registered Successfully", HttpStatus.OK);

    }
}
