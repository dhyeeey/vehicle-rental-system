package org.intech.vehiclerental.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface CustomAccountDetailsService extends UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email);
}
