package com.shimanshu.security.service;

import com.shimanshu.security.dto.CustomerDTO;
import com.shimanshu.security.entity.AccessToken;
import com.shimanshu.security.entity.Customer;
import com.shimanshu.security.repository.AccessTokenRepository;
import com.shimanshu.security.repository.CustomerRepository;
import com.shimanshu.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CustomerService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccessTokenRepository accessTokenRepository;

    @Autowired
    CustomerRepository customerRepository;

    public ResponseEntity<?> viewProfile(String accessToken1){
        AccessToken accessToken = accessTokenRepository.findByToken(accessToken1);
        LocalDateTime time = accessToken.getExpireAt();
        if (time.isBefore(LocalDateTime.now())){
            throw new RuntimeException("token is Expired");
        }
        Optional<Customer> customer;
        customer = customerRepository.findByEmail(accessToken.getUserEntity().getEmail());
        return new ResponseEntity<>("Customer User Id: "+customer.get().getId()+
                "\nCustomer First Name: "+customer.get().getFirstName()+
                "\nCustomer Last Name: "+customer.get().getLastName()+
                "\nCustomer active Status: "+customer.get().getActive()+
                "\nCustomer contact: "+customer.get().getContact(), HttpStatus.OK);
    }

    public ResponseEntity<Customer>updateProfile(){
        return null;
    }
}
