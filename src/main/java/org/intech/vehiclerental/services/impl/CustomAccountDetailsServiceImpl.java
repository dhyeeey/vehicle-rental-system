package org.intech.vehiclerental.services.impl;

import org.intech.vehiclerental.exceptions.WrongLoginEmailCredentialException;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.CustomUserDetails;
import org.intech.vehiclerental.repositories.AccountOwnerRepository;
import org.intech.vehiclerental.services.CustomAccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomAccountDetailsServiceImpl implements CustomAccountDetailsService {
    private final AccountOwnerRepository accountOwnerRepository;

    @Autowired
    public CustomAccountDetailsServiceImpl(AccountOwnerRepository accountOwnerRepository) {
        this.accountOwnerRepository = accountOwnerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)throws UsernameNotFoundException {
        AccountOwner accountOwner =
                accountOwnerRepository.findByEmail(email)
                        .orElseThrow(() -> new WrongLoginEmailCredentialException("Failed to find account"));

        return new CustomUserDetails(accountOwner);
    }
}
