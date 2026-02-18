package org.intech.vehiclerental.services;

import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.Company;
import org.intech.vehiclerental.models.User;
import org.intech.vehiclerental.repositories.AccountOwnerRepository;
import org.intech.vehiclerental.repositories.CompanyRepository;
import org.intech.vehiclerental.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountOwnerService {

    private UserRepository userRepository;
    private CompanyRepository companyRepository;
    private AccountOwnerRepository accountOwnerRepository;

    @Autowired
    public AccountOwnerService(UserRepository userRepository,
                               CompanyRepository companyRepository,
                               AccountOwnerRepository accountOwnerRepository
                               ){
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.accountOwnerRepository = accountOwnerRepository;
    }

    public AccountOwner findAccountById(Long accountId){
        AccountOwner accountOwner = accountOwnerRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account id"));

        return accountOwner;
    }

    public AccountOwner findAccountByEmail(String email) {
        AccountOwner accountOwner = accountOwnerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email id"));

        return accountOwner;
    }

    public User findUserById(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));

        return user;
    }

    public Company findCompanyById(Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid company id"));

        return company;
    }
}
