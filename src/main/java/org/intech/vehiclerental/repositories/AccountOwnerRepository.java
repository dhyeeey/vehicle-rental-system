package org.intech.vehiclerental.repositories;

import org.intech.vehiclerental.models.AccountOwner;

import java.util.Optional;

public interface AccountOwnerRepository {

    Optional<AccountOwner> findById(Long id);

    Optional<AccountOwner> findByEmail(String email);

    boolean existsByEmail(String email);

    AccountOwner save(AccountOwner accountOwner);
}