package org.intech.vehiclerental.services;

import org.intech.vehiclerental.dto.requestbody.CreateAccountPayloadBody;
import org.intech.vehiclerental.dto.requestbody.LoginPayloadBody;
import org.intech.vehiclerental.entities.AccountOwner;
import org.intech.vehiclerental.entities.Company;
import org.intech.vehiclerental.entities.User;
import org.intech.vehiclerental.entities.enums.AccountType;
import org.intech.vehiclerental.exceptions.PasswordMismatchException;
import org.intech.vehiclerental.exceptions.WrongLoginEmailCredentialException;
import org.intech.vehiclerental.exceptions.WrongLoginPasswordCredentialException;
import org.intech.vehiclerental.repositories.AccountOwnerRepository;
import org.intech.vehiclerental.repositories.CompanyRepository;
import org.intech.vehiclerental.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginAndRegistrationService {

    private UserRepository userRepository;
    private CompanyRepository companyRepository;
    private AccountOwnerRepository accountOwnerRepository;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;

    @Autowired
    public LoginAndRegistrationService(
            UserRepository userRepository,
            CompanyRepository companyRepository,
            AccountOwnerRepository accountOwnerRepository,
            PasswordEncoder passwordEncoder,
            ModelMapper modelMapper
    ){
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.accountOwnerRepository = accountOwnerRepository;
    }

    public AccountOwner findAccountOwnerByEmailAndPassword(LoginPayloadBody loginPayloadBody){
        AccountOwner accountOwner = accountOwnerRepository.findByEmail(loginPayloadBody.email())
                .orElseThrow(() -> new WrongLoginEmailCredentialException("Invalid email"));

        if (accountOwner instanceof User user) {
            if(!passwordEncoder.matches(loginPayloadBody.password(), user.getPassword())){
                throw new WrongLoginPasswordCredentialException("Invalid password");
            }
        }

        else if (accountOwner instanceof Company company) {
            if(!passwordEncoder.matches(loginPayloadBody.password(), company.getPassword())){
                throw new WrongLoginPasswordCredentialException("Invalid password");
            }
        }

        return accountOwner;
    }

    public User findLoginUserByEmailAndPassword(LoginPayloadBody loginPayloadBody){
        User user = userRepository.findByEmail(loginPayloadBody.email())
                        .orElseThrow(() -> new WrongLoginEmailCredentialException("Invalid email"));

        if(!passwordEncoder.matches(loginPayloadBody.password(), user.getPassword())){
            throw new WrongLoginPasswordCredentialException("Invalid password");
        }

        return user;
    }

    public Company findLoginCompanyByEmailAndPassword(LoginPayloadBody loginPayloadBody){
        Company company = companyRepository.findByEmail(loginPayloadBody.email())
                .orElseThrow(() -> new WrongLoginEmailCredentialException("Invalid email"));

        if(!passwordEncoder.matches(loginPayloadBody.password(), company.getPassword())){
            throw new WrongLoginPasswordCredentialException("Invalid password");
        }

        return company;
    }

    public User saveUser(CreateAccountPayloadBody createAccountPayloadBody){
//        User user = modelMapper.map(createAccountPayloadBody, User.class);

        if (!createAccountPayloadBody.password().equals(createAccountPayloadBody.confirmPassword())) {
            throw new PasswordMismatchException("Your password and confirm password do not match");
        }

        User user = User.builder()
                .firstName(createAccountPayloadBody.firstName())
                .lastName(createAccountPayloadBody.lastName())
                .phoneNumber(createAccountPayloadBody.phoneNumber())
                .licenseNumber(createAccountPayloadBody.licenseNumber())
                .build();

        user.setEmail(createAccountPayloadBody.email());
        user.setPassword(passwordEncoder.encode(createAccountPayloadBody.password()));
        user.setAccountType(AccountType.INDIVIDUAL);

        User savedUser = userRepository.save(user);

        return savedUser;
    }
}

