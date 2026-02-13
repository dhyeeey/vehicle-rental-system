package org.intech.vehiclerental.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.intech.vehiclerental.dto.requestbody.CreateAccountPayloadBody;
import org.intech.vehiclerental.dto.requestbody.LoginPayloadBody;
import org.intech.vehiclerental.entities.User;
import org.intech.vehiclerental.services.LoginAndRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class LoginAndRegistrationController {

    private LoginAndRegistrationService loginAndRegistrationService;

    @Autowired
    public LoginAndRegistrationController(LoginAndRegistrationService loginAndRegistrationService){
        this.loginAndRegistrationService = loginAndRegistrationService;
    }

    @PostMapping(value="/login")
    public ResponseEntity<?> login(HttpServletRequest httpServletRequest,
                                   @Valid @RequestBody(required = true) LoginPayloadBody loginPayloadBody
    ){

        User user = loginAndRegistrationService
                .findLoginUserByEmailAndPassword(loginPayloadBody);

        HttpSession session = httpServletRequest.getSession(true);
        httpServletRequest.changeSessionId();

        session.setAttribute("USER_ID", user.getId());

        return ResponseEntity.ok(user);
    }

    @PostMapping(
            value="/createaccount",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createAccount(
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody(required = true) CreateAccountPayloadBody createAccountPayloadBody){
        User user = loginAndRegistrationService.saveUser(createAccountPayloadBody);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest){
//
//    }

}
