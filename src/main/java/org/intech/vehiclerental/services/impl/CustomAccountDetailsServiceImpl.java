package org.intech.vehiclerental.services.impl;

import org.intech.vehiclerental.dto.authdto.AuthUserProjection;
import org.intech.vehiclerental.exceptions.WrongLoginEmailCredentialException;
import org.intech.vehiclerental.models.CustomUserDetails;
import org.intech.vehiclerental.repositories.custom.AccountOwnerQueryRepository;
import org.intech.vehiclerental.services.CustomAccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomAccountDetailsServiceImpl implements CustomAccountDetailsService {
    private final AccountOwnerQueryRepository accountOwnerQueryRepository;

    @Autowired
    public CustomAccountDetailsServiceImpl(AccountOwnerQueryRepository accountOwnerQueryRepository) {
        this.accountOwnerQueryRepository = accountOwnerQueryRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)throws UsernameNotFoundException {
        AuthUserProjection user =
                accountOwnerQueryRepository.findAuthDetailsByEmail(email)
                        .orElseThrow(() -> new WrongLoginEmailCredentialException("Failed to find account"));

        return new CustomUserDetails(
                user.id(),
                user.email(),
                user.password(),
                user.role(),
                user.accountStatus()
        );
    }
}
