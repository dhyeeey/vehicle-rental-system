package org.intech.vehiclerental.services;

import org.intech.vehiclerental.entities.User;
import org.intech.vehiclerental.repositories.CompanyRepository;
import org.intech.vehiclerental.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private CompanyRepository companyRepository;

    @Autowired
    public UserService(UserRepository userRepository, CompanyRepository companyRepository){
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public User findUserById(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));

        return user;
    }


}
