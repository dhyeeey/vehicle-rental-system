package org.intech.vehiclerental.controllers;

import org.intech.vehiclerental.models.CustomUserDetails;
import org.intech.vehiclerental.services.AccountOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            Authentication authentication,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

//        String email = authentication.getName();
//
//        AccountOwner accountOwner =
//                accountOwnerService.findAccountByEmail(email);

        return ResponseEntity.ok(customUserDetails.getAccountOwner());
    }

}
