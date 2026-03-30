package org.intech.vehiclerental.controllers;

import org.intech.vehiclerental.dto.requestbody.EditAccountProfileDto;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.CustomUserDetails;
import org.intech.vehiclerental.services.AccountOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final AccountOwnerService accountOwnerService;

    @Autowired
    public UserController(AccountOwnerService accountOwnerService){
        this.accountOwnerService = accountOwnerService;
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountOwner> getProfile(
            Authentication authentication
    ) {
        String email = authentication.getName();

        AccountOwner accountOwner =
                accountOwnerService.findAccountByEmail(email);

        return ResponseEntity.ok(accountOwner);
    }

    @PatchMapping("/profile/remove-profile-image")
    public ResponseEntity<Void> removeProfileImage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        accountOwnerService.removeProfileImage(customUserDetails.getId());

        return ResponseEntity.ok().build();
    }

    @PutMapping("/profile/edit-profile")
    public ResponseEntity<Integer> editProfileDetails(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody EditAccountProfileDto editAccountProfileDto
    ){
        int val = accountOwnerService.editProfileDetails(userDetails.getId(), editAccountProfileDto);

        return ResponseEntity.ok(val);
    }

    @PutMapping("/profile/edit-profile-image")
    public ResponseEntity<Void> editProfileImage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "file", required = true) MultipartFile file
    ){
        accountOwnerService.editProfileImage(userDetails.getId(), file);
        return ResponseEntity.ok().build();
    }

}
