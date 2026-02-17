package org.intech.vehiclerental.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.intech.vehiclerental.dto.requestbody.CreateAccountPayloadBody;
import org.intech.vehiclerental.dto.requestbody.LoginPayloadBody;
import org.intech.vehiclerental.entities.AccountOwner;
import org.intech.vehiclerental.entities.User;
import org.intech.vehiclerental.services.LoginAndRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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
        AccountOwner accountOwner = loginAndRegistrationService
                                        .findAccountOwnerByEmailAndPassword(loginPayloadBody);

        // Build authorities based on account type
        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + accountOwner.getAccountType().name()));

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        accountOwner.getEmail(),   // principal
                        null,
                        authorities
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        HttpSession session = httpServletRequest.getSession(true);
        httpServletRequest.changeSessionId();

        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context
        );

        return ResponseEntity.ok().build();
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return ResponseEntity.ok().build();
    }


}
