package org.intech.vehiclerental.repositories;

import org.intech.vehiclerental.dto.requestbody.EditAccountProfileDto;
import org.intech.vehiclerental.models.AccountOwner;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface AccountOwnerRepository {

    Optional<AccountOwner> findById(Long id);

    Optional<AccountOwner> findByEmail(String email);

    boolean existsByEmail(String email);

    AccountOwner save(AccountOwner accountOwner);

    int editProfileDetails(Long accountOwnerId, EditAccountProfileDto editAccountProfileDto);

    int editProfileImage(AccountOwner accountOwner, String imageUrl);
    int editProfileImage(Long accountOwnerId, String imageUrl);


    String getCurrentProfileImageUrl(Long accountOwnerId);

}