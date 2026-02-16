package org.intech.vehiclerental.services;

import org.intech.vehiclerental.dto.requestbody.CreateAccountPayloadBody;
import org.intech.vehiclerental.dto.requestbody.LoginPayloadBody;
import org.intech.vehiclerental.entities.User;
import org.intech.vehiclerental.exceptions.WrongLoginEmailCredentialException;
import org.intech.vehiclerental.exceptions.WrongLoginPasswordCredentialException;
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
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;

    @Autowired
    public LoginAndRegistrationService(
            UserRepository userRepository,
            CompanyRepository companyRepository,
            PasswordEncoder passwordEncoder,
            ModelMapper modelMapper
    ){
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public User findLoginUserByEmailAndPassword(LoginPayloadBody loginPayloadBody){
        User user = userRepository.findByEmail(loginPayloadBody.email())
                        .orElseThrow(() -> new WrongLoginEmailCredentialException("Invalid email"));

        if(!passwordEncoder.matches(loginPayloadBody.password(), user.getPassword())){
            throw new WrongLoginPasswordCredentialException("Invalid password");
        }

        return user;
    }

    public User saveUser(CreateAccountPayloadBody createAccountPayloadBody){
//        User user = modelMapper.map(createAccountPayloadBody, User.class);

        if (!createAccountPayloadBody.password().equals(createAccountPayloadBody.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = User.builder()
                .firstName(createAccountPayloadBody.firstName())
                .lastName(createAccountPayloadBody.lastName())
                .email(createAccountPayloadBody.email())
                .phoneNumber(createAccountPayloadBody.phoneNumber())
                .password(createAccountPayloadBody.password())
                .licenseNumber(createAccountPayloadBody.licenseNumber())
                .build();


        System.out.println("Mapped user: " + user.toString());

        user.setPassword(passwordEncoder.encode(createAccountPayloadBody.password()));

        User savedUser = userRepository.save(user);

        return savedUser;
    }
}

