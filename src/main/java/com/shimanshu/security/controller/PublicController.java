package com.shimanshu.security.controller;

import com.shimanshu.security.dto.AuthResponseDto;
import com.shimanshu.security.dto.LoginDto;
import com.shimanshu.security.dto.RegisterDto;
import com.shimanshu.security.entity.Customer;
import com.shimanshu.security.entity.Role;
import com.shimanshu.security.repository.CustomerRepository;
import com.shimanshu.security.repository.RoleRepository;
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
    private CustomerRepository customerRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncode;

    private JWTGenerator jwtGenerator;

    @Autowired
    public PublicController(AuthenticationManager authenticationManager, CustomerRepository customerRepository, RoleRepository roleRepository, PasswordEncoder passwordEncode, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.passwordEncode = passwordEncode;
        this.jwtGenerator = jwtGenerator;
    }




    @PostMapping("login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto ){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token),HttpStatus.OK);

    }

    @GetMapping("hello")
    public String display(){
        return "hello";
    }
    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        if(customerRepository.existsByEmail(registerDto.getEmail())){
            return new ResponseEntity<>("Email is already taken", HttpStatus.BAD_REQUEST);
        }

        Customer customer = new Customer();
        customer.setEmail(registerDto.getEmail());
        customer.setPassword(passwordEncode.encode(registerDto.getPassword()));

        System.out.println(customer);
        Role roles = roleRepository.findByRole("CUSTOMER").get();
        customer.setRoles(Collections.singletonList(roles));

        customerRepository.save(customer);

        return new ResponseEntity<>("Customer Registered Successfully",HttpStatus.OK);

    }
}
