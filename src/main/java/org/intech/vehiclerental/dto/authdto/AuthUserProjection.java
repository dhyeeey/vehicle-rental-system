package org.intech.vehiclerental.dto.authdto;

import org.intech.vehiclerental.models.enums.AccountStatus;
import org.intech.vehiclerental.models.enums.Role;

public record AuthUserProjection(
        Long id,
        String email,
        String password,
        Role role,
        AccountStatus accountStatus
) {}