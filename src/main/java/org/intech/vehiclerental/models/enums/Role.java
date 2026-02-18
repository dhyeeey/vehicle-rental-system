package org.intech.vehiclerental.models.enums;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_INDIVIDUAL,
    ROLE_COMPANY,
    ROLE_ADMIN;

    @Override
    public @Nullable String getAuthority() {
        return name();
    }
}
