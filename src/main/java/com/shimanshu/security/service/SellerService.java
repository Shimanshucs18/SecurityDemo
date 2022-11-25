package com.shimanshu.security.service;

import com.shimanshu.security.Exception.TokenExpiredException;
import com.shimanshu.security.dto.AddAddressDto;
import com.shimanshu.security.dto.ChangePasswordDto;
import com.shimanshu.security.dto.UpdateSellerDto;
import com.shimanshu.security.entity.AccessToken;
import com.shimanshu.security.entity.Address;
import com.shimanshu.security.entity.Seller;
import com.shimanshu.security.entity.UserEntity;
import com.shimanshu.security.repository.AccessTokenRepository;
import com.shimanshu.security.repository.AddressRepository;
import com.shimanshu.security.repository.SellerRepository;
import com.shimanshu.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SellerService {

    @Autowired
    AccessTokenRepository accessTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    MailSender mailSender;

    @Autowired
    AddressRepository addressRepository;

    public ResponseEntity<?> viewSellerProfile(String accessToken){
        AccessToken token = accessTokenRepository.findByToken(accessToken).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = token.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token Expired!!");
        }
        if (userRepository.existsByEmail(token.getUserEntity().getEmail())){
            log.info("User exists!");
            UserEntity userEntity = userRepository.findUserByEmail(token.getUserEntity().getEmail());
            List<Object[]> list = addressRepository.findByUserId(userEntity.getId());
            Address address = addressRepository.findByUId(userEntity.getId());
            log.info("returning a list of Object.");
            return new ResponseEntity<>("Seller User Id: "+userEntity.getId()+"\nSeller First name: "+userEntity.getFirstName()+"\nSeller Last name: "+userEntity.getLastName()+"\nSeller active status: "+userEntity.getIsActive()+"\nSeller companyContact: "+sellerRepository.getCompanyContactOfUserId(userEntity.getId())+"\nSeller companyName: "+sellerRepository.getCompanyNameOfUserId(userEntity.getId())+"\nSeller gstNumber: "+sellerRepository.getGstNumberOfUserId(userEntity.getId())+"\nSeller Address: \n"+address.toString(), HttpStatus.OK);
        } else {
            log.info("Couldn't find address related to user!!!");
            return new ResponseEntity<>("Error fetching addresses",HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> updateSellerProfile(UpdateSellerDto updateSellerDto){
        String token = updateSellerDto.getAccessToken();
        AccessToken accessToken = accessTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException("Invalid Access Token"));
        LocalDateTime expireAdt = accessToken.getExpireAt();
        if (expireAdt.isBefore(LocalDateTime.now())){
            throw new TokenExpiredException("Access Token Expired!!");
        }
        if (userRepository.existsByEmail(accessToken.getUserEntity().getEmail())){
            log.info("User Exists");
            UserEntity userEntity = userRepository.findUserByEmail(accessToken.getUserEntity().getEmail());
            userEntity.setFirstName(updateSellerDto.getFirstName());
            userEntity.setMiddleName(updateSellerDto.getMiddleName());
            userEntity.setLastName(updateSellerDto.getLastName());
            userEntity.setEmail(updateSellerDto.getEmail());
            Seller seller = sellerRepository.getSellerByUserId(userEntity.getId());
            seller.setCompanyContact(updateSellerDto.getCompanyContact());
            seller.setCompanyName(updateSellerDto.getCompanyName());
            seller.setGstNumber(updateSellerDto.getGstNumber());
            userRepository.save(userEntity);
            sellerRepository.save(seller);
            log.info("user updated!!");

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Profile Updated");
            mailMessage.setText("ALERT!, Your profile has been updated, If it was not you contact Admin asap.\nStay Safe, Thanks.");
            mailMessage.setTo(userEntity.getEmail());
            mailMessage.setFrom("shimanshu.sharma@tothenew.com");
            Date date = new Date();
            mailMessage.setSentDate(date);
            try {
                mailSender.send(mailMessage);
            }catch (MailException e){
                log.info("Error sending Mail");
            }
            return new ResponseEntity<>("User profile Updated!",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Could not update the profile!",HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<?> changeSellerPassword(ChangePasswordDto changePasswordDto) {
        String token = changePasswordDto.getAccessToken();
        AccessToken accessToken = accessTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = accessToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }

        if (userRepository.existsByEmail(accessToken.getUserEntity().getEmail())) {
            UserEntity userEntity = userRepository.findUserByEmail(accessToken.getUserEntity().getEmail());
            userEntity.setPassword(passwordEncoder.encode(changePasswordDto.getPassword()));
            log.info("Changed password and encoded, then saved it.");
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Password Changed");
            mailMessage.setText("ALERT!, Your account's password has been changed, If it was not you contact Admin asap.\nStay Safe, Thanks.");
            mailMessage.setTo(userEntity.getEmail());
            mailMessage.setFrom("shimanshu.sharma@tothenew.com");
            Date date = new Date();
            mailMessage.setSentDate(date);
            try {
                mailSender.send(mailMessage);
            } catch (MailException e) {
                log.info("Error sending mail");
            }
            return new ResponseEntity<>("Changed Password Successfully!", HttpStatus.OK);
        } else  {
            log.info("Failed to change password!");
            return new ResponseEntity<>("Failed to change password!", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateSellerAddress(Long id, AddAddressDto addAddressDto) {
        String token = addAddressDto.getAccessToken();
        AccessToken accessToken = accessTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = accessToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }

        if (userRepository.existsByEmail(accessToken.getUserEntity().getEmail())) {
            UserEntity userEntity = userRepository.findUserByEmail(accessToken.getUserEntity().getEmail());
            log.info("user exists");

            if (addressRepository.existsById(id)) {
                log.info("address exists");
                Address address = addressRepository.findByUId(id);
                address.setAddressLine(addAddressDto.getAddress());
                address.setLabel(addAddressDto.getLabel());
                address.setZipcode(addAddressDto.getZipcode());
                address.setCountry(addAddressDto.getCountry());
                address.setState(addAddressDto.getState());
                address.setCity(addAddressDto.getCity());
                log.info("trying to save the updated address");
                addressRepository.save(address);
                return new ResponseEntity<>("Address updated successfully.", HttpStatus.OK);
            } else {
                Address address = new Address();
                address.setUserEntity(userEntity);
                address.setAddressLine(addAddressDto.getAddress());
                address.setCity(addAddressDto.getCity());
                address.setCountry(addAddressDto.getCountry());
                address.setState(addAddressDto.getState());
                address.setZipcode(addAddressDto.getZipcode());
                address.setLabel(addAddressDto.getLabel());
                addressRepository.save(address);
                log.info("Address added to the respected user");
                return new ResponseEntity<>("Added the address.", HttpStatus.CREATED);
            }
        } else {
            log.info("No address exists");
            return new ResponseEntity<>(String.format("No address exists with address id: "+id), HttpStatus.NOT_FOUND);
        }
    }


}
