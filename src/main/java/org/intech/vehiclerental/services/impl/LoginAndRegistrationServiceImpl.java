package org.intech.vehiclerental.services.impl;

import org.intech.vehiclerental.dto.requestbody.CreateAccountPayloadBody;
import org.intech.vehiclerental.exceptions.PasswordMismatchException;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.User;
import org.intech.vehiclerental.models.enums.AccountStatus;
import org.intech.vehiclerental.models.enums.Role;
import org.intech.vehiclerental.repositories.custom.AccountOwnerQueryRepository;
import org.intech.vehiclerental.repositories.datajpa.AccountOwnerRepository;
import org.intech.vehiclerental.services.LoginAndRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginAndRegistrationServiceImpl
        implements LoginAndRegistrationService {

    private final PasswordEncoder passwordEncoder;
    private final AccountOwnerRepository accountOwnerRepository;

    @Autowired
    public LoginAndRegistrationServiceImpl(
            PasswordEncoder passwordEncoder,
            AccountOwnerRepository accountOwnerRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.accountOwnerRepository = accountOwnerRepository;
    }

    @Override
    @Transactional
    public User registerUser(CreateAccountPayloadBody payload) {

        if (!payload.password().equals(payload.confirmPassword())) {
            throw new PasswordMismatchException(
                    "Your password and confirm password do not match");
        }

        if (accountOwnerRepository.existsByEmail(payload.email())) {
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

        return (User) accountOwnerRepository.save(accountOwner);
    }
}