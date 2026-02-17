package org.intech.vehiclerental.services;

import org.intech.vehiclerental.entities.AccountOwner;
import org.intech.vehiclerental.repositories.AccountOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomAccountDetailsService implements UserDetailsService {

    private final AccountOwnerRepository accountOwnerRepository;

    @Autowired
    public CustomAccountDetailsService(AccountOwnerRepository accountOwnerRepository) {
        this.accountOwnerRepository = accountOwnerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        AccountOwner accountOwner =
                accountOwnerRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Failed to find account"));

        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(
                        "ROLE_" + accountOwner.getAccountType().name()
                ));

        return new org.springframework.security.core.userdetails.User(
                email,
                accountOwner.getPassword(),
                authorities
        );

    }
}
