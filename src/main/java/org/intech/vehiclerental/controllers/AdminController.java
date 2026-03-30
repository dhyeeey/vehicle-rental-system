package org.intech.vehiclerental.controllers;

import lombok.extern.slf4j.Slf4j;
import org.intech.vehiclerental.dto.admin.ListUserAccountAdminView;
import org.intech.vehiclerental.dto.admin.UserDetailAdminDto;
import org.intech.vehiclerental.dto.paginationdto.PageResponse;
import org.intech.vehiclerental.dto.rentaldto.RentalInfo;
import org.intech.vehiclerental.dto.requestbody.ChangeVehicleStatusDto;
import org.intech.vehiclerental.dto.vehicledto.VehicleInfo;
import org.intech.vehiclerental.dto.vehicledto.VehicleListViewAdmin;
import org.intech.vehiclerental.models.CustomUserDetails;
import org.intech.vehiclerental.models.enums.VehicleApprovalStatus;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.intech.vehiclerental.services.AccountOwnerService;
import org.intech.vehiclerental.services.AdminService;
import org.intech.vehiclerental.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAnyRole('ADMIN','COMPANY')")
@RestController
@RequestMapping(value="/api/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminController {

    private final VehicleService vehicleService;
    private final AccountOwnerService accountOwnerService;
    private final AdminService adminService;

    @Autowired
    public AdminController(VehicleService vehicleService,
                           AccountOwnerService accountOwnerService,
                           AdminService adminService){
        this.vehicleService = vehicleService;
        this.accountOwnerService = accountOwnerService;
        this.adminService = adminService;
    }

    @GetMapping(value="/vehicles/list-all")
    public ResponseEntity<PageResponse<VehicleListViewAdmin>> getVehicleListForAdminAndCompanyByStatus(
            @RequestParam(required = false) VehicleStatus vehicleStatus,
            @RequestParam(required = false) VehicleApprovalStatus vehicleApprovalStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(
                new PageResponse<>(
                        vehicleService
                            .getVehicleListForAdminAndCompanyByStatus(
                                    vehicleStatus,
                                    vehicleApprovalStatus,
                                    page,
                                    size
                            )
                )
        );
    }

    @GetMapping(value="/vehicle/{vehicleId}")
    public ResponseEntity<VehicleInfo> getVehicleDetailForAdmin(
            @PathVariable Long vehicleId
    ){
        return ResponseEntity.ok(vehicleService.findVehicleInfoById(vehicleId).orElseGet(()->null));
    }

    @GetMapping("/list-users")
    public ResponseEntity<PageResponse<ListUserAccountAdminView>> getUsersListForAdmin(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ){
        return ResponseEntity.ok(new PageResponse<>(adminService.findUsersListForAdmin(pageable)));
    }

    @GetMapping("/user-detail/{userId}")
    public ResponseEntity<UserDetailAdminDto> getUserDetailForAdmin(@PathVariable Long userId){
        return ResponseEntity.ok(adminService.findUserDetailForAdmin(userId));
    }

    @GetMapping("/user-detail/{userId}/rentals")
    public ResponseEntity<List<RentalInfo>> getUserRentalsForAdmin(@PathVariable Long userId){
        return ResponseEntity.ok(adminService.findRentalsOfUserForAdmin(userId));
    }

    @GetMapping("/user-detail/{userId}/vehicles")
    public ResponseEntity<List<VehicleInfo>> getUserVehiclesForAdmin(@PathVariable Long userId){
        return ResponseEntity.ok(adminService.findUserVehiclesForAdmin(userId));
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<Void> deleteUser(@RequestParam Long userIdToBeDeleted){
        accountOwnerService.deleteUser(userIdToBeDeleted);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/change-vehicle-status")
    public ResponseEntity<Integer> changeVehicleApprovalStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ChangeVehicleStatusDto dto
    ){

        int val = vehicleService.changeVehicleApprovalStatus(
                dto.vehicleId(),
                dto.vehicleStatus(),
                dto.vehicleApprovalStatus(),
                userDetails.getId()
        );

        return ResponseEntity.ok(val);
    }


}
