package org.intech.vehiclerental.services;

import org.intech.vehiclerental.models.AccountOwner;

public interface AccountOwnerService {

    AccountOwner findAccountByEmail(String email);

    AccountOwner findByIdOrThrow(Long id);

    boolean emailExists(String email);
}