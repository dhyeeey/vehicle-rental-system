package org.intech.vehiclerental.services.impl;

import org.intech.vehiclerental.dto.requestbody.CreateAccountPayloadBody;
import org.intech.vehiclerental.exceptions.PasswordMismatchException;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.User;
import org.intech.vehiclerental.models.enums.AccountStatus;
import org.intech.vehiclerental.models.enums.Role;
import org.intech.vehiclerental.repositories.AccountOwnerRepository;
import org.intech.vehiclerental.services.LoginAndRegistrationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginAndRegistrationServiceImpl
        implements LoginAndRegistrationService {

    private final AccountOwnerRepository repository;
    private final PasswordEncoder passwordEncoder;

    public LoginAndRegistrationServiceImpl(
            AccountOwnerRepository repository,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(CreateAccountPayloadBody payload) {

        if (!payload.password().equals(payload.confirmPassword())) {
            throw new PasswordMismatchException(
                    "Your password and confirm password do not match");
        }

        if (repository.existsByEmail(payload.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        AccountOwner accountOwner = (AccountOwner) User.builder()
                .firstName(payload.firstName())
                .lastName(payload.lastName())
                .licenseNumber(payload.licenseNumber())
                .build();


        accountOwner.setAccountStatus(AccountStatus.ACTIVE);
        accountOwner.setPhoneNumber(payload.phoneNumber());
        accountOwner.setEmail(payload.email());
        accountOwner.setPassword(passwordEncoder.encode(payload.password()));
        accountOwner.setRole(Role.ROLE_INDIVIDUAL);

        return (User) repository.save(accountOwner);
    }
}