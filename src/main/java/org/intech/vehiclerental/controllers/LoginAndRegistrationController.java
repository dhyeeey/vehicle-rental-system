package org.intech.vehiclerental.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.intech.vehiclerental.dto.requestbody.CreateAccountPayloadBody;
import org.intech.vehiclerental.dto.requestbody.LoginPayloadBody;
import org.intech.vehiclerental.models.CustomUserDetails;
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
import org.springframework.web.bind.annotation.*;

import java.util.Map;


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

    @GetMapping("/me")
    public ResponseEntity<?> checkSession(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(Map.of(
                "id", user.getAccountOwner().getId(),
                "email", user.getUsername(),
                "role", user.getAccountOwner().getRole()
        ));
    }

    @PostMapping(value="/login")
    public ResponseEntity<?> login(
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody(required = true) LoginPayloadBody loginPayloadBody
    ){
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginPayloadBody.email(),
                                loginPayloadBody.password()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Create or get session
        HttpSession session = httpServletRequest.getSession(true);

        // Store security context in session
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        // regenerate session ID for security
        httpServletRequest.changeSessionId();

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
