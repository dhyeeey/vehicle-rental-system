package org.intech.vehiclerental.services.impl;

import org.intech.vehiclerental.dto.admin.ListUserAccountAdminView;
import org.intech.vehiclerental.dto.admin.UserDetailAdminDto;
import org.intech.vehiclerental.dto.rentaldto.RentalInfo;
import org.intech.vehiclerental.repositories.custom.AdminQueryRepository;
import org.intech.vehiclerental.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    public AdminQueryRepository adminQueryRepository;

    @Autowired
    public AdminServiceImpl(AdminQueryRepository adminQueryRepository){
        this.adminQueryRepository = adminQueryRepository;
    }

    @Override
    public List<ListUserAccountAdminView> findUserListForAdmin() {
        return adminQueryRepository.findUsersListForAdmin();
    }

    @Override
    public List<RentalInfo> findRentalsOfUserForAdmin(Long userId) {
        return adminQueryRepository.findRentalsOfUserForAdmin(userId);
    }

    @Override
    public UserDetailAdminDto findUserDetailForAdmin(Long userId) {
        return adminQueryRepository.findUserDetailForAdmin(userId);
    }
}
