package org.intech.vehiclerental.services;

import com.blazebit.persistence.PagedList;
import org.intech.vehiclerental.dto.admin.ListUserAccountAdminView;
import org.intech.vehiclerental.dto.admin.UserDetailAdminDto;
import org.intech.vehiclerental.dto.rentaldto.RentalInfo;
import org.intech.vehiclerental.dto.vehicledto.VehicleInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {
    PagedList<ListUserAccountAdminView> findUsersListForAdmin(Pageable pageable);
    List<RentalInfo> findRentalsOfUserForAdmin(Long userId);
    UserDetailAdminDto findUserDetailForAdmin(Long userId);
    List<VehicleInfo> findUserVehiclesForAdmin(Long userId);
}
