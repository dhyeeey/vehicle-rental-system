package org.intech.vehiclerental.controllers;

import com.blazebit.persistence.PagedList;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.intech.vehiclerental.dto.paginationdto.PageResponse;
import org.intech.vehiclerental.dto.requestbody.VehicleRegistrationDTO;
import org.intech.vehiclerental.dto.requestbody.VehicleStatusUpdateRequest;
import org.intech.vehiclerental.dto.vehicledto.*;
import org.intech.vehiclerental.models.CustomUserDetails;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.services.ImageValidationService;
import org.intech.vehiclerental.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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


    /*--------------------------------------------GET--------------------------------------------------- */

    @GetMapping("/detail/{vehicleId}")
    public ResponseEntity<?> getVehicleDetailsPublic(
            @PathVariable(value = "vehicleId") Long vehicleId
    ){
        VehicleInfo vehicle = vehicleService.findVehicleInfoById(vehicleId).orElseThrow(
                ()->new RuntimeException("Vehicle not found")
        );

        return ResponseEntity.ok(vehicle);
    }


    @GetMapping("/detail/edit-form/{vehicleId}")
    public ResponseEntity<?> getVehicleDetails(
            @PathVariable(value = "vehicleId") Long vehicleId
    ){
        VehicleInfo vehicle = vehicleService.findVehicleInfoById(vehicleId).orElseThrow(
                ()->new RuntimeException("Vehicle not found")
        );

        return ResponseEntity.ok(vehicle);
    }

    @GetMapping("/explore/search")
    public ResponseEntity<?> getAllSearchVehicles(
            Authentication authentication,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        List<VehicleSearchInfo> vehicles;

        if(authentication != null && authentication.isAuthenticated()){
             vehicles = vehicleService.findVehicleSearchSetByDifferentOwner(
                    customUserDetails.getId()
            );
        }else{
            vehicles = vehicleService.findVehicleSearchSetByDifferentOwner(
                    null
            );
        }

        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/all-fleet")
    public ResponseEntity<?> getAllFleetVehicles(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {

        PagedList<VehicleFleetDto> vehiclePage =
                vehicleService.findVehicleFleetPageByOwner(
                        userDetails.getId(),
                        null,
                        true,
                        pageable
                );

        return ResponseEntity.ok(new PageResponse<>(vehiclePage));
    }

    /*--------------------------------------------POST--------------------------------------------------- */

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RegisterVehicleResponseDTO> registerVehicle(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute VehicleRegistrationDTO dto,
            @RequestParam(value = "images", required = true) List<MultipartFile> images
    ) {

        imageValidationService.validateImages(images, dto.primaryImageIndex());
        Vehicle vehicle = vehicleService.registerVehicle(dto, images, dto.primaryImageIndex(), userDetails.getId());

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
            @Valid @ModelAttribute VehicleRegistrationDTO dto,
            @RequestParam(value = "images", required = true) List<MultipartFile> images
    ) {
        log.info("Alternative endpoint - Received vehicle registration request");
        return registerVehicle(userDetails, dto, images);
    }


    /*--------------------------------------------PATCH--------------------------------------------------- */

    @PatchMapping("/status")
    public ResponseEntity<?> changeVehicleStatus(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody VehicleStatusUpdateRequest vehicleStatusUpdateRequest
    ){
        vehicleService.changeVehicleStatus(
                vehicleStatusUpdateRequest.vehicleId(),
                vehicleStatusUpdateRequest.status(), customUserDetails.getId()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping(
            value = "/edit-vehicle-details/{vehicleId}"
    )
    public ResponseEntity<?> updateVehicle(
            @PathVariable Long vehicleId,
            @RequestBody VehicleUpdateFormData dto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(vehicleService.updateVehiclePartial(vehicleId, dto));
    }

    /*--------------------------------------------DELETE--------------------------------------------------- */

    @DeleteMapping
    public ResponseEntity<?> deleteVehicleFromFleet(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long vehicleId
    ){
        int val = vehicleService.deleteVehicleById(vehicleId, customUserDetails.getId());
        return ResponseEntity.ok(val);
    }


}
