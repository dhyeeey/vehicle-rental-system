package org.intech.vehiclerental.controllers;

import org.intech.vehiclerental.dto.rentaldto.CreateRentalRequestDto;
import org.intech.vehiclerental.mappers.RentalMapper;
import org.intech.vehiclerental.models.*;
import org.intech.vehiclerental.services.RentalService;
import org.intech.vehiclerental.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/rental")
public class RentalController {

    private VehicleService vehicleService;
    private RentalService rentalService;
    private RentalMapper rentalMapper;

    @Autowired
    public RentalController(VehicleService vehicleService, RentalService rentalService){
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRental(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody CreateRentalRequestDto createRentalRequestDto
    ){
        AccountOwner owner = customUserDetails.getAccountOwner();

        if (!(owner instanceof User user)) {
            throw new RuntimeException("Only users can rent vehicles");
        }

        Vehicle vehicle = vehicleService
                .findVehicleEntityWithOwnerById(createRentalRequestDto.vehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Rental rental = rentalService.createRental(user, vehicle, createRentalRequestDto);

        return ResponseEntity.ok(rentalMapper.toCreateRentalResponseDtoFromRental(rental));
    }

}
