package org.intech.vehiclerental.repositories.custom;

import org.intech.vehiclerental.dto.admin.ListUserAccountAdminView;
import org.intech.vehiclerental.dto.authdto.AuthUserProjection;
import org.intech.vehiclerental.dto.requestbody.EditAccountProfileDto;
import org.intech.vehiclerental.models.AccountOwner;

import java.util.List;
import java.util.Optional;

public interface AccountOwnerQueryRepository {

    Optional<AccountOwner> findById(Long id);

    Optional<AccountOwner> findByEmail(String email);

    boolean existsByEmail(String email);

    AccountOwner save(AccountOwner accountOwner);

    int editProfileDetails(Long accountOwnerId, EditAccountProfileDto editAccountProfileDto);

    int editProfileImage(AccountOwner accountOwner, String imageUrl);
    int editProfileImage(Long accountOwnerId, String imageUrl);


    String getCurrentProfileImageUrl(Long accountOwnerId);

    void deleteUser(Long userId);

    Optional<AuthUserProjection> findAuthDetailsByEmail(String email);

}