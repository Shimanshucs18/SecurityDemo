package com.shimanshu.security.controller;

import com.shimanshu.security.entity.Customer;
import com.shimanshu.security.entity.Seller;
import com.shimanshu.security.repository.CustomerRepository;
import com.shimanshu.security.repository.SellerRepository;
import com.shimanshu.security.service.UserPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminApiController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    UserPasswordService userPasswordService;

    @GetMapping("/customers")
    public List<Customer>getAllCustomers(){
        return customerRepository.findAll();
    }

    @GetMapping("/sellers")
    public List<Seller>getAllSeller(){return sellerRepository.findAll();}

    @PutMapping("customer/activate/{id}")
    public ResponseEntity<?> activateCustomer(@PathVariable Long id){
        return userPasswordService.confirmById(id);
    }

    @PatchMapping("customer/deactivate/{id}")
    public ResponseEntity<?> deactivateCustomer(@PathVariable("id") Long id){
        return userPasswordService.removeById(id);
    }

    @PutMapping("seller/activate/{id}")
    public ResponseEntity<?> activateSeller(@PathVariable Long id){return userPasswordService.confirmById(id);}

    @PutMapping("customer/deactivate/{id}")
    public ResponseEntity<?> deactivatedSeller(@PathVariable("id") Long id){
        return userPasswordService.removeById(id);
    }

}
