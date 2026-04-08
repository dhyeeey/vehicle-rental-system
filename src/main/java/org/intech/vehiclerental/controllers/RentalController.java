package org.intech.vehiclerental.controllers;

import com.blazebit.persistence.PagedList;
import org.intech.vehiclerental.dto.paginationdto.PageResponse;
import org.intech.vehiclerental.dto.rentaldto.*;
import org.intech.vehiclerental.dto.requestbody.ChangeRentalStatus;
import org.intech.vehiclerental.dto.requestbody.SubmitReviewPayload;
import org.intech.vehiclerental.mappers.RentalMapper;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.CustomUserDetails;
import org.intech.vehiclerental.models.Rental;
import org.intech.vehiclerental.models.User;
import org.intech.vehiclerental.models.enums.RentalStatus;
import org.intech.vehiclerental.services.AccountOwnerService;
import org.intech.vehiclerental.services.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/rental", produces = MediaType.APPLICATION_JSON_VALUE)
public class RentalController {

    private AccountOwnerService accountOwnerService;
    private RentalService rentalService;
    private RentalMapper rentalMapper;

    @Autowired
    public RentalController(
            RentalService rentalService,
            AccountOwnerService accountOwnerService,
            RentalMapper rentalMapper
    ){
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
        this.accountOwnerService = accountOwnerService;
    }

    @PostMapping("/create")
    public CreateRentalResponseDto createRental(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreateRentalRequestDto createRentalRequestDto
    ){
        AccountOwner owner = accountOwnerService.findByIdOrThrow(userDetails.getId());

        if (!(owner instanceof User user)) {
            throw new RuntimeException("Only users can rent vehicles");
        }

        Rental rental = rentalService.createRental(user, createRentalRequestDto);

        return rentalMapper.toCreateRentalResponseDtoFromRental(rental);
    }

    @GetMapping("/vehicle/{vehicleId}/requests")
    public List<RentalViewForRequests> findRentalRequestsByVehicleId(
            @PathVariable(value = "vehicleId") Long vehicleId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return rentalService.findRentalRequestsByVehicleId(vehicleId, customUserDetails.getId());
    }

    @PostMapping("/change-status")
    public Integer changeRentalStatus(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ChangeRentalStatus dto
    ){
        return rentalService.changeRentalStatus(
                dto.rentalId(),dto.status(), customUserDetails.getId()
        );
    }

    @GetMapping("/{rentalId}/review")
    public ReviewData fetchExistingReview(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long rentalId
    ){
        return rentalService.fetchExistingReviewOfRental(rentalId, customUserDetails.getId());
    }

    @PostMapping("/add-review/{rentalId}")
    public ResponseEntity<?> addVehicleReviewForRental(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long rentalId,
            @RequestBody SubmitReviewPayload submitReviewPayload
    ){
        rentalService.addRentalReview(submitReviewPayload, customUserDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/rental-request-detail/{rentalId}")
    public RentalDetailViewForRentalRequest findRentalRequestDetailByRentalId(
            @PathVariable Long rentalId
    ){
        return rentalService.findRentalDetailViewForRentalRequest(rentalId);
    }


    @GetMapping("/list-all")
    public PageResponse<RentalListDto> fetchAllRentals(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        AccountOwner accountOwner = accountOwnerService.findByIdOrThrow(userDetails.getId());

        if (!(accountOwner instanceof User user)) {
            throw new RuntimeException("Only users can rent vehicles");
        }

        PagedList<RentalListDto> rentalListDtoPage =
                rentalService.findRentalPageByRenter(
                        user,
                        RentalStatus.PENDING,
                        pageable
                );

        return new PageResponse<>(rentalListDtoPage);
    }

}
