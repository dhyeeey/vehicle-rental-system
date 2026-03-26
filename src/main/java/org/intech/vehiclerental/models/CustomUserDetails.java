package org.intech.vehiclerental.models;

import lombok.Getter;
import org.intech.vehiclerental.models.enums.Role;
import org.intech.vehiclerental.models.enums.AccountStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    private final Long id;
    private final String email;
    private final String password;
    private final Role role;
    private final AccountStatus accountStatus;

    public CustomUserDetails(
            Long id,
            String email,
            String password,
            Role role,
            AccountStatus accountStatus
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.accountStatus = accountStatus;
    }


    public Role getAccountType() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return accountStatus == AccountStatus.ACTIVE;
    }

}
