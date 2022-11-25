package com.shimanshu.security.service;

import com.shimanshu.security.Exception.TokenExpiredException;
import com.shimanshu.security.dto.AddAddressDto;
import com.shimanshu.security.dto.ChangePasswordDto;
import com.shimanshu.security.dto.CustomerDTO;
import com.shimanshu.security.dto.UpdateCustomerDto;
import com.shimanshu.security.entity.AccessToken;
import com.shimanshu.security.entity.Address;
import com.shimanshu.security.entity.Customer;
import com.shimanshu.security.entity.UserEntity;
import com.shimanshu.security.repository.AccessTokenRepository;
import com.shimanshu.security.repository.AddressRepository;
import com.shimanshu.security.repository.CustomerRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccessTokenRepository accessTokenRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    MailSender mailSender;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    CustomerRepository customerRepository;

    public ResponseEntity<?> viewMyProfile(String accessToken) {
        AccessToken token = accessTokenRepository.findByToken(accessToken).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = token.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }
        UserEntity userEntity = userRepository.findUserByEmail(token.getUserEntity().getEmail());
        return new ResponseEntity<>("Customer User Id: " + userEntity.getId() + "\nCustomer First name: " + userEntity.getFirstName() + "\nCustomer Last name: " + userEntity.getLastName() + "\nCustomer active status: " + userEntity.getIsActive() + "\nCustomer contact: " + customerRepository.getContactOfUserId(userEntity.getId()), HttpStatus.OK);
    }

    public ResponseEntity<?> changePassword(ChangePasswordDto changePasswordDto) {
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

    public ResponseEntity<?> addNewAddress(AddAddressDto addAddressDto) {
        String token = addAddressDto.getAccessToken();
        AccessToken accessToken = accessTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = accessToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }
        if (userRepository.existsByEmail(accessToken.getUserEntity().getEmail())) {
            UserEntity userEntity = userRepository.findUserByEmail(accessToken.getUserEntity().getEmail());
            log.info("user exists");
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
        } else {
            log.info("Failed to add address.");
            return new ResponseEntity<>("Unable to add address!", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateAddress(Long id, AddAddressDto addAddressDto) {
        String token = addAddressDto.getAccessToken();
        AccessToken accessToken = accessTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = accessToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }
        if (addressRepository.existsById(id)) {
            log.info("address exists");
            Address address = addressRepository.getById(id);
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
            log.info("No address exists");
            return new ResponseEntity<>(String.format("No address exists with address id: "+id), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> deleteAddress(String accessToken, Long id) {
        AccessToken token = accessTokenRepository.findByToken(accessToken).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = token.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }
        if (addressRepository.existsById(id)) {
            log.info("Address exists.");
            addressRepository.deleteById(id);
            log.info("deletion successful");
            return new ResponseEntity<>("Deleted Address Successfully.", HttpStatus.OK);
        } else {
            log.info("deletion failed!");
            return new ResponseEntity<>(String.format("No address found with associating address id: ", id), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> viewMyAddresses(String accessToken) {
        AccessToken token = accessTokenRepository.findByToken(accessToken).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = token.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }
        if (userRepository.existsByEmail(token.getUserEntity().getEmail())) {
            log.info("User exists!");
            UserEntity userEntity = userRepository.findUserByEmail(token.getUserEntity().getEmail());
            List<Object[]> list = addressRepository.findByUserId(userEntity.getId());
            log.info("returning a list of objects.");
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            log.info("Couldn't find address related to user!!!");
            return new ResponseEntity<>("Error fetching addresses", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> updateProfile(UpdateCustomerDto updateCustomerDto) {
        String token = updateCustomerDto.getAccessToken();
        AccessToken accessToken = accessTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = accessToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }
        if (userRepository.existsByEmail(accessToken.getUserEntity().getEmail())) {
            log.info("User exists.");
            UserEntity userEntity = userRepository.findUserByEmail(accessToken.getUserEntity().getEmail());
            userEntity.setFirstName(updateCustomerDto.getFirstName());
            userEntity.setMiddleName(updateCustomerDto.getMiddleName());
            userEntity.setLastName(updateCustomerDto.getLastName());
            userEntity.setEmail(updateCustomerDto.getEmail());
            Customer customer = customerRepository.getCustomerByUserId(userEntity.getId());
            customer.setContact(updateCustomerDto.getContact());
            userRepository.save(userEntity);
            customerRepository.save(customer);
            log.info("user updated!");

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Profile Updated");
            mailMessage.setText("ALERT!, Your profile has been updated, If it was not you contact Admin asap.\nStay Safe, Thanks.");
            mailMessage.setTo(userEntity.getEmail());
            mailMessage.setFrom("shimanshu.sharma@tothenew.com");
            Date date = new Date();
            mailMessage.setSentDate(date);
            try {
                mailSender.send(mailMessage);
            } catch (MailException e) {
                log.info("Error sending mail");
            }
            return new ResponseEntity<>("User profile updated!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Could not update the profile!", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> uploadImage(String accessToken, MultipartFile multipartFile) {
        AccessToken token = accessTokenRepository.findByToken(accessToken).orElseThrow(() -> new IllegalStateException("Invalid Access Token!"));
        LocalDateTime expiredAt = token.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Access Token expired!!");
        }
        if (userRepository.existsByEmail(token.getUserEntity().getEmail())) {
            log.info("User exists.");
            UserEntity userEntity = userRepository.findUserByEmail(token.getUserEntity().getEmail());

            try {
                if (multipartFile.isEmpty()) {
                    log.info("Empty file");
                    return new ResponseEntity<>("Image cannot be empty!!", HttpStatus.BAD_REQUEST);
                }
                if (!multipartFile.getContentType().equals("image/jpeg") &&
                        !multipartFile.getContentType().equals("image/png") &&
                        !multipartFile.getContentType().equals("image/bmp")) {

                    log.info("Illegal image format.");
                    return new ResponseEntity<>("Only JPEG,JPG,PNG & BMP extensions are allowed!!", HttpStatus.BAD_REQUEST);
                }
                String fileName = multipartFile.getOriginalFilename();
                log.info("Old name: " + fileName);
                String[] fileNameSplits = fileName.split("\\.");
                int extensionIndex = fileNameSplits.length - 1;
                String newName = userEntity.getId()+"."+fileNameSplits[extensionIndex];
                log.info("New name: " + newName);

                Files.copy(multipartFile.getInputStream(), Paths.get(FileUploaderForUsersUtil.UPLOAD_DIR+ File.separator+newName), StandardCopyOption.REPLACE_EXISTING);


                log.info("Image uploaded!");
                return new ResponseEntity<>("Updated the profile with latest image provided.", HttpStatus.OK);
            } catch (Exception exception) {
                log.error("Cannot upload image!");
                return new ResponseEntity<>("Cannot upload Image!", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Could not load the image!", HttpStatus.BAD_REQUEST);
        }
    }


}

