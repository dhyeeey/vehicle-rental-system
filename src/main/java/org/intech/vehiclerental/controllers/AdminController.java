package org.intech.vehiclerental.controllers;

import lombok.extern.slf4j.Slf4j;
import org.intech.vehiclerental.dto.requestbody.ChangeVehicleApprovalStatusDto;
import org.intech.vehiclerental.models.CustomUserDetails;
import org.intech.vehiclerental.models.enums.VehicleApprovalStatus;
import org.intech.vehiclerental.models.enums.VehicleStatus;
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

    @Autowired
    public AdminController(VehicleService vehicleService){
        this.vehicleService = vehicleService;
    }

    @GetMapping(value="/vehicles/list-all")
    public ResponseEntity<?> getVehicleListForAdminAndCompanyByStatus(
            @RequestParam(required = false) VehicleStatus vehicleStatus,
            @RequestParam(required = false) VehicleApprovalStatus vehicleApprovalStatus
    ){
        return ResponseEntity.ok(vehicleService
                                    .getVehicleListForAdminAndCompanyByStatus(vehicleStatus,vehicleApprovalStatus));
    }

    @GetMapping(value="/vehicle/{vehicleId}")
    public ResponseEntity<?> getVehicleDetailForAdmin(
            @PathVariable Long vehicleId
    ){
        return ResponseEntity.ok(vehicleService.findVehicleInfoById(vehicleId).orElseGet(()->null));
    }

    @PatchMapping("/approve-vehicle")
    public ResponseEntity<?> changeVehicleApprovalStatus(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ChangeVehicleApprovalStatusDto dto){

        int val = vehicleService.changeVehicleApprovalStatus(
                dto.vehicleId(),
                dto.vehicleStatus(),
                dto.vehicleApprovalStatus(),
                customUserDetails.getAccountOwner()
        );

        return ResponseEntity.ok(val);
    }

//    @GetMapping("/vehicle/{vehicleId}")
//    public ResponseEntity<?> getVehicleDetails
}
