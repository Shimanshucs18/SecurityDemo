package com.shimanshu.security.controller;

import com.shimanshu.security.dto.*;
import com.shimanshu.security.entity.AccessToken;
import com.shimanshu.security.entity.Customer;
import com.shimanshu.security.entity.Role;
import com.shimanshu.security.entity.UserEntity;
import com.shimanshu.security.repository.AccessTokenRepository;
import com.shimanshu.security.repository.CustomerRepository;
import com.shimanshu.security.repository.RoleRepository;
import com.shimanshu.security.repository.UserRepository;
import com.shimanshu.security.security.JWTGenerator;
import com.shimanshu.security.service.CustomerService;
import com.shimanshu.security.service.EmailSenderService;
import com.shimanshu.security.service.UserPasswordService;
import com.shimanshu.security.service.UserService;
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
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/auth")
public class PublicController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncode;
    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private UserPasswordService userPasswordService;

    @Autowired
    CustomerService customerService;


    @Autowired
    public PublicController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, UserService userService, CustomerRepository customerRepository, PasswordEncoder passwordEncode, JWTGenerator jwtGenerator, AccessTokenRepository accessTokenRepository, UserPasswordService userPasswordService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
        this.userService = userService;
        this.passwordEncode = passwordEncode;
        this.jwtGenerator = jwtGenerator;
        this.accessTokenRepository = accessTokenRepository;
        this.userPasswordService = userPasswordService;
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        UserEntity user = userRepository.findByEmail(loginDto.getEmail()).get();
        AccessToken accessToken = new AccessToken();
        accessToken.setToken(token);
        accessToken.setEmail(loginDto.getEmail());
        accessToken.setUserEntity(user);

        accessTokenRepository.save(accessToken);
        System.out.println(token);

        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);

    }

    @GetMapping("hello")
    public String display() {
        return "hello";
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDao registerDao) {
        if (userRepository.existsByEmail(registerDao.getEmail())) {
            return new ResponseEntity<>("Email is already taken", HttpStatus.BAD_REQUEST);
        }
        Customer customer = new Customer();
        customer.setContact(registerDao.getEmail());
        customer.setPassword(passwordEncode.encode(registerDao.getPassword()));
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(registerDao.getEmail());
        userEntity.setPassword(passwordEncode.encode(registerDao.getPassword()));
        userEntity.setCustomer(customer);
        System.out.println(userEntity);
        Role roles = roleRepository.findByAuthority("CUSTOMER").get();
        customer.setRoles(Collections.singletonList(roles));
        String ID = UUID.randomUUID().toString();
        String URL = "http://localhost:9091/seller/activate?token=" + ID;
        userService.saveUUIDTokenWithEmail(registerDao.getEmail(),ID);
        emailSenderService.sendEmail(registerDao.getEmail(),"Email Response", URL);



        userRepository.save(customer);

        return new ResponseEntity<>("Customer Registered Successfully", HttpStatus.OK);

    }

    @PutMapping("/activate")
    ResponseEntity activateUser(@RequestParam("token") String activationToken){
        return userService.validateToken(activationToken);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
        return userPasswordService.forgotPassword(forgotPasswordDto.getEmail());
    }

    @PostMapping("/set-password")
    public ResponseEntity<String> setPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        return userPasswordService.resetUserPassword(resetPasswordDto);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> customerProfile(@RequestParam("accessToken") String accessToken) {
        return customerService.viewProfile(accessToken);
    }

    @PutMapping("/update_profile")
    public ResponseEntity<Customer> updateProfile(@RequestBody Customer customer) {
        return customerService.updateProfile();
    }
}
