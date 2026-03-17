package org.intech.vehiclerental.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.intech.vehiclerental.dto.requestbody.CreateAccountPayloadBody;
import org.intech.vehiclerental.dto.requestbody.LoginPayloadBody;
import org.intech.vehiclerental.models.User;
import org.intech.vehiclerental.services.LoginAndRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class LoginAndRegistrationController {

    private final AuthenticationManager authenticationManager;
    private final LoginAndRegistrationService loginAndRegistrationService;

    @Autowired
    public LoginAndRegistrationController(
            LoginAndRegistrationService loginAndRegistrationService,
            AuthenticationManager authenticationManager
    ){
        this.loginAndRegistrationService = loginAndRegistrationService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value="/login")
    public ResponseEntity<?> login(
            HttpServletRequest request,
            @RequestBody(required = false) LoginPayloadBody body,
            Authentication authentication
    ){
        // CASE 1: Check existing session
        if (body == null || (body.email() == null && body.password() == null)) {

            if (authentication != null && authentication.isAuthenticated()) {
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // CASE 2: Normal login
        Authentication auth =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                body.email(),
                                body.password()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        int ONE_WEEK = 7 /* Days*/
                * 24  /* Hours */
                * 60  /* Minute */
                * 60  /*Second */;

        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(ONE_WEEK);

        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        request.changeSessionId();

        return ResponseEntity.ok().build();
    }

    @PostMapping(
            value="/createaccount",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createAccount(
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody(required = true) CreateAccountPayloadBody createAccountPayloadBody
    ){
        User user = loginAndRegistrationService.registerUser(createAccountPayloadBody);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
