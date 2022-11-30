package com.shimanshu.security.controller;

import com.shimanshu.security.RegistrationConfig.RegistrationService;
import com.shimanshu.security.entity.UserEntity;
import com.shimanshu.security.repository.SellerRepository;
import com.shimanshu.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

     @Autowired
    SellerRepository sellerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RegistrationService registrationService;

    @GetMapping("/admin-board")
    public String adminAccess() {
        return "Admin Board.";
    }

    @GetMapping(path = "/seller/confirm")
    public String confirmSeller(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

    @GetMapping("/user-list")
    public @ResponseBody
    List<UserEntity> returnUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/customer-list")
    public @ResponseBody List<Object[]> returnCustomers() {
        return userRepository.printPartialDataForCustomers();
    }

    @GetMapping("/seller-list")
    public @ResponseBody List<Object[]> returnSellers() {
        List<Object[]> list = new ArrayList<>();
        list.addAll(userRepository.printPartialDataForSellers());
        return list;
    }

    @PatchMapping("/activate/customer/{id}")
    public ResponseEntity<?> activateCustomer(@PathVariable("id") Long id) {
        return registrationService.confirmById(id);
    }

    @PatchMapping("/deactivate/customer/{id}")
    public ResponseEntity<?> deactivateCustomer(@PathVariable("id") Long id) {
        return registrationService.disableById(id);
    }

    @PatchMapping("/activate/seller/{id}")
    public ResponseEntity<?> activateSeller(@PathVariable("id") Long id) {
        return registrationService.confirmById(id);
    }

    @PatchMapping("/deactivate/seller/{id}")
    public ResponseEntity<?> deactivateSeller(@PathVariable("id") Long id) {
        return registrationService.disableById(id);
    }
//
//    @GetMapping("/customers")
//    public List<Customer>getAllCustomers(){
//        return customerRepository.findAll();
//    }
//
//    @GetMapping("/sellers")
//    public List<Seller>getAllSeller(){return sellerRepository.findAll();}
//
//    @PutMapping("customer/activate/{id}")
//    public ResponseEntity<?> activateCustomer(@PathVariable Long id){
//        return userPasswordService.confirmById(id);
//    }
//
//    @PatchMapping("customer/deactivate/{id}")
//    public ResponseEntity<?> deactivateCustomer(@PathVariable("id") Long id){
//        return userPasswordService.removeById(id);
//    }
//
//    @PutMapping("seller/activate/{id}")
//    public ResponseEntity<?> activateSeller(@PathVariable Long id){return userPasswordService.confirmById   (id);}
//
//    @PutMapping("customer/deactivate/{id}")
//    public ResponseEntity<?> deactivatedSeller(@PathVariable("id") Long id){
//        return userPasswordService.removeById(id);
//    }

}
