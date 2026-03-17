package org.intech.vehiclerental.controllers;

import lombok.extern.slf4j.Slf4j;
import org.intech.vehiclerental.dto.paginationdto.PageResponse;
import org.intech.vehiclerental.dto.requestbody.ChangeVehicleStatusDto;
import org.intech.vehiclerental.models.CustomUserDetails;
import org.intech.vehiclerental.models.enums.VehicleApprovalStatus;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.intech.vehiclerental.services.AccountOwnerService;
import org.intech.vehiclerental.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasAnyRole('ADMIN','COMPANY')")
@RestController
@RequestMapping(value="/api/admin")
@Slf4j
public class AdminController {

    private final VehicleService vehicleService;
    private final AccountOwnerService accountOwnerService;

    @Autowired
    public AdminController(VehicleService vehicleService, AccountOwnerService accountOwnerService){
        this.vehicleService = vehicleService;
        this.accountOwnerService = accountOwnerService;
    }

    @GetMapping(value="/vehicles/list-all")
    public ResponseEntity<?> getVehicleListForAdminAndCompanyByStatus(
            @RequestParam(required = false) VehicleStatus vehicleStatus,
            @RequestParam(required = false) VehicleApprovalStatus vehicleApprovalStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(new PageResponse<>(vehicleService
                .getVehicleListForAdminAndCompanyByStatus(vehicleStatus,vehicleApprovalStatus, page, size)));
    }

    @GetMapping(value="/vehicle/{vehicleId}")
    public ResponseEntity<?> getVehicleDetailForAdmin(
            @PathVariable Long vehicleId
    ){
        return ResponseEntity.ok(vehicleService.findVehicleInfoById(vehicleId).orElseGet(()->null));
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestParam Long userIdToBeDeleted){
        accountOwnerService.deleteUser(userIdToBeDeleted);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/change-vehicle-status")
    public ResponseEntity<?> changeVehicleApprovalStatus(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ChangeVehicleStatusDto dto
    ){

        int val = vehicleService.changeVehicleApprovalStatus(
                dto.vehicleId(),
                dto.vehicleStatus(),
                dto.vehicleApprovalStatus(),
                customUserDetails.getAccountOwner()
        );

        return ResponseEntity.ok(val);
    }


}
