package org.intech.vehiclerental.services;

import org.intech.vehiclerental.dto.admin.ListUserAccountAdminView;
import org.intech.vehiclerental.dto.requestbody.EditAccountProfileDto;
import org.intech.vehiclerental.models.AccountOwner;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AccountOwnerService {

    AccountOwner findAccountByEmail(String email);

    AccountOwner findByIdOrThrow(Long id);

    boolean emailExists(String email);

    int editProfileDetails(Long accountOwnerId, EditAccountProfileDto editAccountProfileDto);

    void editProfileImage(Long accountOwnerId, MultipartFile file);

    void removeProfileImage(Long accountOwnerId);

    void deleteUser(Long userId);

}