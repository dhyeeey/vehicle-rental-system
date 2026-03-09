package org.intech.vehiclerental.controllers;

import com.blazebit.persistence.PagedList;
import org.intech.vehiclerental.dto.paginationdto.PageResponse;
import org.intech.vehiclerental.dto.rentaldto.CreateRentalRequestDto;
import org.intech.vehiclerental.dto.rentaldto.RentalListDto;
import org.intech.vehiclerental.mappers.RentalMapper;
import org.intech.vehiclerental.models.*;
import org.intech.vehiclerental.models.enums.RentalStatus;
import org.intech.vehiclerental.services.RentalService;
import org.intech.vehiclerental.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rental")
public class RentalController {

    private VehicleService vehicleService;
    private RentalService rentalService;
    private RentalMapper rentalMapper;

    @Autowired
    public RentalController(VehicleService vehicleService,
                            RentalService rentalService,
                            RentalMapper rentalMapper){
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
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

    @GetMapping("/vehicle/{vehicleId}/requests")
    public ResponseEntity<?> findRentalRequestsByVehicleId(
            @PathVariable(value = "vehicleId") Long vehicleId
    ){
        return ResponseEntity.ok(rentalService.findRentalRequestsByVehicleId(vehicleId));
    }


    @GetMapping("/all")
    public ResponseEntity<?> fetchAllRentals(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        Pageable pageable = PageRequest.of(0,
                10,
                Sort.by("createdAt").descending()
        );

        AccountOwner accountOwner = customUserDetails.getAccountOwner();

        if (!(accountOwner instanceof User user)) {
            throw new RuntimeException("Only users can rent vehicles");
        }

        PagedList<RentalListDto> rentalListDtoPage = rentalService.findRentalPageByRenter(
                user,
                RentalStatus.PENDING,
                pageable
        );

        return ResponseEntity.ok(new PageResponse<>(rentalListDtoPage));
    }

}
