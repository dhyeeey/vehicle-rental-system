package org.intech.vehiclerental.services.impl;

import org.intech.vehiclerental.dto.requestbody.EditAccountProfileDto;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.repositories.custom.AccountOwnerQueryRepository;
import org.intech.vehiclerental.repositories.datajpa.AccountOwnerRepository;
import org.intech.vehiclerental.services.AccountOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private final AccountOwnerQueryRepository accountOwnerQueryRepository;
    private final AccountOwnerRepository accountOwnerRepository;

    @Value("${app.upload.profile.image.dir}")
    private String profileImagesDir;

    @Autowired
    public AccountOwnerServiceImpl(
            AccountOwnerQueryRepository accountOwnerQueryRepository,
            AccountOwnerRepository accountOwnerRepository
    ) {
        this.accountOwnerQueryRepository = accountOwnerQueryRepository;
        this.accountOwnerRepository = accountOwnerRepository;
    }

    @Override
    public AccountOwner findAccountByEmail(String email) {
        return accountOwnerRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email id"));
    }

    @Override
    public AccountOwner findByIdOrThrow(Long id) {
        return accountOwnerRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Account not found"));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId){
        accountOwnerRepository.deleteById(userId);
    }

    @Override
    public boolean emailExists(String email) {
        return accountOwnerRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public int editProfileDetails(Long accountOwnerId, EditAccountProfileDto editAccountProfileDto) {
        return accountOwnerQueryRepository.editProfileDetails(accountOwnerId, editAccountProfileDto);
    }

    @Override
    @Transactional
    public void removeProfileImage(Long accountOwnerId){
        String oldProfileImageUrl = accountOwnerQueryRepository.getCurrentProfileImageUrl(accountOwnerId);

        accountOwnerQueryRepository.editProfileImage(accountOwnerId, null);

        if (oldProfileImageUrl != null && !oldProfileImageUrl.isBlank()) {

            Path oldImagePath = Paths.get(oldProfileImageUrl.replace("/uploads/", "uploads/"));

            try {
                Files.deleteIfExists(oldImagePath);
            } catch (IOException ioException) {
                System.err.println("Failed to delete old image: " + oldImagePath);
            }
        }
    }

    @Override
    @Transactional
    public void editProfileImage(Long accountOwnerId, MultipartFile file) {

        AccountOwner accountOwner = accountOwnerRepository.findById(accountOwnerId)
                .orElseThrow(()->new RuntimeException("Account with provided id not found"));

        String oldImageUrl = accountOwner.getProfileImageUrl();

        try {

            Path uploadPath = Paths.get(profileImagesDir);

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

            String imageUrl = "/"+profileImagesDir + fileName;

            accountOwnerQueryRepository.editProfileImage(accountOwner, imageUrl);

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