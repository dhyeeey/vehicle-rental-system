package org.intech.vehiclerental.services.impl;

import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.repositories.AccountOwnerRepository;
import org.intech.vehiclerental.services.AccountOwnerService;
import org.springframework.stereotype.Service;

@Service
public class AccountOwnerServiceImpl implements AccountOwnerService {

    private final AccountOwnerRepository repository;

    public AccountOwnerServiceImpl(AccountOwnerRepository repository) {
        this.repository = repository;
    }

    @Override
    public AccountOwner findAccountByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email id"));
    }

    @Override
    public AccountOwner findByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Account not found"));
    }

    @Override
    public boolean emailExists(String email) {
        return repository.existsByEmail(email);
    }
}