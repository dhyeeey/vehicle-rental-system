package org.intech.vehiclerental.services.impl;

import org.intech.vehiclerental.dto.requestbody.EditAccountProfileDto;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.repositories.AccountOwnerRepository;
import org.intech.vehiclerental.services.AccountOwnerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

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

    @Override
    @Transactional
    public int editProfileDetails(Long accountOwnerId, EditAccountProfileDto editAccountProfileDto) {
        return repository.editProfileDetails(accountOwnerId, editAccountProfileDto);
    }

    @Override
    @Transactional
    public void editProfileImage(Long accountOwnerId, MultipartFile file) {

        AccountOwner accountOwner = repository.findById(accountOwnerId)
                .orElseThrow(()->new RuntimeException("Account with provided id not found"));

        String uploadDir = "uploads/profile-images";

        String oldImageUrl = accountOwner.getProfileImageUrl();

        try {

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

//            String extension = Objects.requireNonNull(file.getOriginalFilename())
//                    .substring(file.getOriginalFilename().lastIndexOf("."));

            String original = Paths.get(Objects.requireNonNull(file.getOriginalFilename()))
                    .getFileName()
                    .toString()
                    .trim()
                    .replaceAll("\\s+", "_");

            String fileName = UUID.randomUUID() + "_" + original;

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = "/uploads/profile-images/" + fileName;

            repository.editProfileImage(accountOwner, imageUrl);

            if (oldImageUrl != null && !oldImageUrl.isBlank()) {

                Path oldImagePath = Paths.get(oldImageUrl.replace("/uploads/", "uploads/"));

                try {
                    Files.deleteIfExists(oldImagePath);
                } catch (IOException ioException) {
                    System.err.println("Failed to delete old image: " + oldImagePath);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }

    }
}