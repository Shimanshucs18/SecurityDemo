package com.shimanshu.security.controller;

import com.shimanshu.security.EmailSenderImpl.EmailSender;
import com.shimanshu.security.Exception.TokenRefreshException;
import com.shimanshu.security.dto.*;
import com.shimanshu.security.entity.*;
import com.shimanshu.security.repository.*;
import com.shimanshu.security.security.JWTGenerator;
import com.shimanshu.security.RegistrationConfig.RegistrationService;
import com.shimanshu.security.security.JwtUtils;
import com.shimanshu.security.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.util.Collections.*;

@RestController
@RequestMapping(value = "/api/auth")
@Slf4j
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
    SellerRepository sellerRepository;

    @Autowired
    private PasswordEncoder passwordEncode;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    MailSender mailSender;

    @Autowired
    CustomerService customerService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    RefreshTokenRepo refreshTokenRepo;
    @Autowired
    private RegistrationService registrationService;

    @Autowired
    EmailSender emailSender;


    @GetMapping("/home")
    public ResponseEntity<?> welcomeHome() {
        return ResponseEntity.ok("You have been logged out.");
    }


    @PostMapping("/customer")
    public ResponseEntity<?> registerAsCustomer(@Valid @RequestBody SignupCustomerDao signupCustomerDao) {
        if (userRepository.existsByEmail(signupCustomerDao.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(signupCustomerDao.getFirstName());
        userEntity.setEmail(signupCustomerDao.getEmail());
        userEntity.setPassword(passwordEncode.encode(signupCustomerDao.getPassword()));
        userEntity.setLastName(signupCustomerDao.getLastName());
        userEntity.setIsActive(false);
        userEntity.setIsDeleted(false);
        userEntity.setIsExpired(false);
        userEntity.setIsLocked(false);
        userEntity.setInvalidAttemptCount(0);

        Customer customer = new Customer();
        userEntity.setCustomer(customer);
        customer.setUserEntity(userEntity);
        customer.setContact(signupCustomerDao.getContact());

        Role roles = roleRepository.findByAuthority("ROLE_CUSTOMER");
        userEntity.setRoles(roles);

//        userRepository.save(userEntity);
        customerRepository.save(customer);

        String token = registrationService.generateToken(userEntity);

        String link = "http://localhost:9091/api/auth/confirm?token=" + token;
        emailSender.send(signupCustomerDao.getEmail(), registrationService.buildEmail(signupCustomerDao.getFirstName(), link));
        return new ResponseEntity<>(
                "Customer Registered Successfully!\nHere is your activation token use it with in 3 hours\n" + token,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/seller")
    public ResponseEntity<?> registerAsSeller(@Valid @RequestBody SignupSellerDao signupSellerDao) {
        if (userRepository.existsByEmail(signupSellerDao.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(signupSellerDao.getFirstName());
        userEntity.setEmail(signupSellerDao.getEmail());
        userEntity.setPassword(passwordEncode.encode(signupSellerDao.getPassword()));
        userEntity.setLastName(signupSellerDao.getLastName());
        userEntity.setIsActive(false);
        userEntity.setIsDeleted(false);
        userEntity.setIsExpired(false);
        userEntity.setIsLocked(false);
        userEntity.setInvalidAttemptCount(0);

        Seller seller = new Seller(userEntity, signupSellerDao.getGstNumber(), signupSellerDao.getCompanyContact(), signupSellerDao.getCompanyName());

        Role roles = roleRepository.findByAuthority("ROLE_CUSTOMER");
        userEntity.setRoles(roles);

        userRepository.save(userEntity);
        sellerRepository.save(seller);

        //Custom email testing part
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("Account Created");
        mailMessage.setText("Congratulations, Your account has been created as Seller.\nContact Admin to activate your account, Thanks.");
        mailMessage.setTo(userEntity.getEmail());
        mailMessage.setFrom("shimanshu.sharma@tothenew.com");
        Date date = new Date();
        mailMessage.setSentDate(date);
        try {
            mailSender.send(mailMessage);
        } catch (MailException e) {
            log.info("Error sending mail");
        }

        return new ResponseEntity<>(
                "Seller Registered Successfully!\nYour account is under approval process from Admin!",
                HttpStatus.CREATED);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAsAdmin(@Valid @RequestBody LoginDto loginDto) {

        UserEntity userEntity = userRepository.findUserByEmail(loginDto.getEmail());

        if (userRepository.isUserActive(userEntity.getId())) {

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            AccessToken accessToken = new AccessToken(jwtUtils.generateJwtToken(userDetails), LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), userEntity);
            accessTokenRepository.save(accessToken);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
            refreshTokenRepo.save(refreshToken);
            String welcomeMessage = "Admin logged in Successfully!!";
            return new ResponseEntity<>(welcomeMessage + "\nAccess Token: " + accessToken.getToken() + "\nRefresh Token: " + refreshToken.getToken(), HttpStatus.OK);

        } else {

            return new ResponseEntity<>("Account is not activated, you cannot login!", HttpStatus.BAD_REQUEST);

        }

    }

    @PostMapping("/customer/login")
    public ResponseEntity<?> loginAsCustomer(@Valid @RequestBody LoginDto loginDto) {

        UserEntity userEntity = userRepository.findUserByEmail(loginDto.getEmail());

        if (userRepository.isUserActive(userEntity.getId())) {

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            AccessToken accessToken = new AccessToken(jwtUtils.generateJwtToken(userDetails), LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), userEntity);
            accessTokenRepository.save(accessToken);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
            refreshTokenRepo.save(refreshToken);
            String welcomeMessage = "Customer logged in Successfully!!";
            return new ResponseEntity<>(welcomeMessage + "\nAccess Token: " + accessToken.getToken() + "\nRefresh Token: " + refreshToken.getToken(), HttpStatus.OK);

        } else {

            return new ResponseEntity<>("Account is not activated, you cannot login!", HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("/seller/login")
    public ResponseEntity<?> loginAsSeller(@Valid @RequestBody LoginDto loginDto) {

        UserEntity userEntity = userRepository.findUserByEmail(loginDto.getEmail());

        if (userRepository.isUserActive(userEntity.getId())) {

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            AccessToken accessToken = new AccessToken(jwtUtils.generateJwtToken(userDetails), LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), userEntity);
            accessTokenRepository.save(accessToken);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
            refreshTokenRepo.save(refreshToken);
            String welcomeMessage = "Seller logged in Successfully!!";
            return new ResponseEntity<>(welcomeMessage + "\nAccess Token: " + accessToken.getToken() + "\nRefresh Token: " + refreshToken.getToken(), HttpStatus.OK);

        } else {

            return new ResponseEntity<>("Account is not activated, you cannot login!", HttpStatus.BAD_REQUEST);

        }

    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserEntity)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getEmail());
                    AccessToken accessToken = new AccessToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
                    accessTokenRepository.save(accessToken);
                    return new ResponseEntity<>("New Access Token: " + accessToken.getToken() + "\nRefresh Token: " + requestRefreshToken, HttpStatus.OK);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PutMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

    @PostMapping(path = "/customer/confirm")
    public String confirmByEmail(@RequestParam("email") String email) {
        return registrationService.confirmByEmail(email);
    }


}