package org.intech.vehiclerental.controllers;

import org.intech.vehiclerental.dto.requestbody.EditAccountProfileDto;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.CustomUserDetails;
import org.intech.vehiclerental.services.AccountOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final AccountOwnerService accountOwnerService;

    @Autowired
    public UserController(AccountOwnerService accountOwnerService){
        this.accountOwnerService = accountOwnerService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(
            Authentication authentication
    ) {

        String email = authentication.getName();

        AccountOwner accountOwner =
                accountOwnerService.findAccountByEmail(email);

        return ResponseEntity.ok(accountOwner);
    }

    @PutMapping("/profile/edit-profile")
    public ResponseEntity<?> editProfileDetails(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody EditAccountProfileDto editAccountProfileDto
    ){
        Long accountOwnerId = customUserDetails.getAccountOwner().getId();
        int val = accountOwnerService.editProfileDetails(accountOwnerId, editAccountProfileDto);

        return ResponseEntity.ok(val);
    }

    @PutMapping("/profile/edit-profile-image")
    public ResponseEntity<?> editProfileImage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(value = "file", required = true) MultipartFile file
    ){
        Long accountOwnerId = customUserDetails.getAccountOwner().getId();
        accountOwnerService.editProfileImage(accountOwnerId, file);

        return ResponseEntity.ok().build();
    }

}
