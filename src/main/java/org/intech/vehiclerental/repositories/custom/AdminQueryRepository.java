package org.intech.vehiclerental.repositories.custom;

import org.intech.vehiclerental.dto.admin.ListUserAccountAdminView;
import org.intech.vehiclerental.dto.admin.UserDetailAdminDto;
import org.intech.vehiclerental.dto.rentaldto.RentalInfo;

import java.util.List;

public interface AdminQueryRepository {
    List<ListUserAccountAdminView> findUsersListForAdmin();
    List<RentalInfo>               findRentalsOfUserForAdmin(Long userId);
    UserDetailAdminDto             findUserDetailForAdmin(Long userId);
}
