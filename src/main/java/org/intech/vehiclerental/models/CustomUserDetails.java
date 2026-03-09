package org.intech.vehiclerental.models;

import org.intech.vehiclerental.models.enums.Role;
import org.intech.vehiclerental.models.enums.AccountStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final AccountOwner accountOwner;

    public CustomUserDetails(AccountOwner accountOwner) {
        this.accountOwner = accountOwner;
    }

    public Long getId() {
        return accountOwner.getId();
    }

    public Role getAccountType() {
        return accountOwner.getRole();
    }

    public AccountOwner getAccountOwner() {
        return accountOwner;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(accountOwner.getRole());
    }


    @Override
    public String getPassword() {
        return accountOwner.getPassword();
    }

    @Override
    public String getUsername() {
        return accountOwner.getEmail();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return accountOwner.getAccountStatus() == AccountStatus.ACTIVE;
    }

}
