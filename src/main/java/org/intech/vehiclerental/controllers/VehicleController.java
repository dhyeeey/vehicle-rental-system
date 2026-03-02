package org.intech.vehiclerental.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.intech.vehiclerental.dto.paginationdto.PageResponse;
import org.intech.vehiclerental.dto.requestbody.VehicleRegistrationDTO;
import org.intech.vehiclerental.dto.vehicledto.RegisterVehicleResponseDTO;
import org.intech.vehiclerental.dto.vehicledto.VehicleFleetDto;
import org.intech.vehiclerental.dto.vehicledto.VehicleInfo;
import org.intech.vehiclerental.dto.vehicledto.VehicleSearchInfo;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.CustomUserDetails;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.intech.vehiclerental.services.ImageValidationService;
import org.intech.vehiclerental.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/vehicle")
@Slf4j
public class VehicleController {

    private final VehicleService vehicleService;
    private final ImageValidationService imageValidationService;

    @Autowired
    public VehicleController(VehicleService vehicleService,
                             ImageValidationService imageValidationService){
        this.vehicleService = vehicleService;
        this.imageValidationService = imageValidationService;
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<?> getVehicleDetails(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable(value = "vehicleId") Long vehicleId
    ){
        VehicleInfo vehicle = vehicleService.findVehicleInfoById(vehicleId).orElseThrow(
                ()->new RuntimeException("Vehicle not found")
        );

        return ResponseEntity.ok(vehicle);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getAllSearchVehicles(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){

        Set<VehicleSearchInfo> vehicles = vehicleService.findVehicleSearchSetByDifferentOwner(
                customUserDetails.getAccountOwner()
        );
        return ResponseEntity.ok(vehicles);

    }

    @GetMapping("/all-fleet")
    public ResponseEntity<?> getAllFleetVehicles(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<VehicleFleetDto> vehiclePage = vehicleService.findVehicleFleetPageByOwner(
               userDetails.getAccountOwner(),
                VehicleStatus.ACTIVE,
                true,
                pageable
        );

        return ResponseEntity.ok(new PageResponse<>(vehiclePage));
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RegisterVehicleResponseDTO> registerVehicle(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute VehicleRegistrationDTO dto,
            @RequestParam(value = "images", required = true) List<MultipartFile> images,
            @RequestParam(value = "primaryImageIndex", defaultValue = "0") Integer primaryImageIndex
    ) {
        AccountOwner accountOwner = userDetails.getAccountOwner();

        imageValidationService.validateImages(images, primaryImageIndex);
        Vehicle vehicle = vehicleService.registerVehicle(dto, images, primaryImageIndex, accountOwner);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegisterVehicleResponseDTO(
                        true,
                        "Vehicle registered successfully",
                        vehicle.getId()
                ));
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RegisterVehicleResponseDTO> registerVehicleAlternative(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("vehicleData") @Valid VehicleRegistrationDTO dto,
            @RequestPart("images") List<MultipartFile> images,
            @RequestParam(value = "primaryImageIndex", defaultValue = "0") Integer primaryImageIndex
    ) {
        AccountOwner accountOwner = userDetails.getAccountOwner();

        log.info("Alternative endpoint - Received vehicle registration request");
        return registerVehicle(userDetails, dto, images, primaryImageIndex);
    }

}
