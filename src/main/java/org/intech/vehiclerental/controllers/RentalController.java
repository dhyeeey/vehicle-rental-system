package org.intech.vehiclerental.controllers;

import com.blazebit.persistence.PagedList;
import org.intech.vehiclerental.dto.paginationdto.PageResponse;
import org.intech.vehiclerental.dto.rentaldto.CreateRentalRequestDto;
import org.intech.vehiclerental.dto.rentaldto.RentalListDto;
import org.intech.vehiclerental.dto.requestbody.ChangeRentalStatus;
import org.intech.vehiclerental.mappers.RentalMapper;
import org.intech.vehiclerental.models.*;
import org.intech.vehiclerental.models.enums.RentalStatus;
import org.intech.vehiclerental.services.AccountOwnerService;
import org.intech.vehiclerental.services.RentalService;
import org.intech.vehiclerental.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rental")
public class RentalController {

    private VehicleService vehicleService;
    private AccountOwnerService accountOwnerService;
    private RentalService rentalService;
    private RentalMapper rentalMapper;

    @Autowired
    public RentalController(VehicleService vehicleService,
                            RentalService rentalService,
                            AccountOwnerService accountOwnerService,
                            RentalMapper rentalMapper){
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
        this.accountOwnerService = accountOwnerService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRental(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreateRentalRequestDto createRentalRequestDto
    ){

        AccountOwner owner = accountOwnerService.findByIdOrThrow(userDetails.getId());

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
        return ResponseEntity
                .ok(rentalService.findRentalRequestsByVehicleId(vehicleId));
    }

    @PostMapping("/change-status")
    public ResponseEntity<?> changeRentalStatus(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ChangeRentalStatus dto
    ){
        if(!rentalService.isCarOwnerAndLoggedUserSame(customUserDetails.getId(),dto.rentalId())){
            throw new AccessDeniedException("You are not allowed to modify this rental");
        }

        return ResponseEntity.ok(rentalService.changeRentalStatus(dto.rentalId(),dto.status()));
    }


    @GetMapping("/list-all")
    public ResponseEntity<?> fetchAllRentals(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ){

        Pageable pageable = PageRequest.of(
                0,
                size,
                 switch(direction){
                    case "desc" -> Sort.by(sortBy).descending();
                    case "asc" -> Sort.by(sortBy).ascending();
                    default  -> Sort.by(sortBy).ascending();
                }
        );

        AccountOwner accountOwner = accountOwnerService.findByIdOrThrow(userDetails.getId());

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
