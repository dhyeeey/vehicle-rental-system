package org.intech.vehiclerental.repositories.custom;

import org.intech.vehiclerental.dto.authdto.AuthUserProjection;
import org.intech.vehiclerental.dto.requestbody.EditAccountProfileDto;
import org.intech.vehiclerental.models.AccountOwner;

import java.util.Optional;

public interface AccountOwnerQueryRepository {

    int editProfileDetails(Long accountOwnerId, EditAccountProfileDto editAccountProfileDto);

    int editProfileImage(AccountOwner accountOwner, String imageUrl);
    int editProfileImage(Long accountOwnerId, String imageUrl);

    String getCurrentProfileImageUrl(Long accountOwnerId);

    Optional<AuthUserProjection> findAuthDetailsByEmail(String email);

}