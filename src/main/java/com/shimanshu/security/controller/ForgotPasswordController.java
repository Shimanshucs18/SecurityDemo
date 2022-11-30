package com.shimanshu.security.controller;

import com.shimanshu.security.dto.ResetPasswordDto;
import com.shimanshu.security.service.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/api/user")
public class ForgotPasswordController {
    @Autowired
    PasswordResetTokenService passwordResetTokenService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Email @RequestParam String email) {
        return passwordResetTokenService.forgotPassword(email);
    }


    @PatchMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        return passwordResetTokenService.resetPassword(resetPasswordDto);
    }

}
