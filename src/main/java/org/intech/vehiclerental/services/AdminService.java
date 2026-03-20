package org.intech.vehiclerental.services;

import org.intech.vehiclerental.dto.admin.ListUserAccountAdminView;
import org.intech.vehiclerental.dto.admin.UserDetailAdminDto;
import org.intech.vehiclerental.dto.rentaldto.RentalInfo;

import java.util.List;

public interface AdminService {
    List<ListUserAccountAdminView> findUserListForAdmin();
    List<RentalInfo> findRentalsOfUserForAdmin(Long userId);
    UserDetailAdminDto findUserDetailForAdmin(Long userId);
}
