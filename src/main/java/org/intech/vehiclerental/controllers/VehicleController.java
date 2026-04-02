package org.intech.vehiclerental.controllers;

import com.blazebit.persistence.PagedList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.intech.vehiclerental.dto.paginationdto.PageResponse;
import org.intech.vehiclerental.dto.requestbody.VehicleRegistrationDTO;
import org.intech.vehiclerental.dto.requestbody.VehicleStatusUpdateRequest;
import org.intech.vehiclerental.dto.vehicledto.*;
import org.intech.vehiclerental.models.CustomUserDetails;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.VehicleImage;
import org.intech.vehiclerental.repositories.utility.VehicleFilter;
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
@RequestMapping(value = "/api/vehicle", produces = MediaType.APPLICATION_JSON_VALUE)
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
    public VehicleInfo getVehicleDetailsPublic(
            @PathVariable(value = "vehicleId") Long vehicleId
    ){
        return vehicleService.findVehicleInfoById(vehicleId).orElseThrow(
                ()->new RuntimeException("Vehicle not found")
        );
    }


    @GetMapping("/detail/edit-form/{vehicleId}")
    public VehicleInfo getVehicleDetails(
            @PathVariable(value = "vehicleId") Long vehicleId
    ){
        return vehicleService.findVehicleInfoById(vehicleId).orElseThrow(
                ()->new RuntimeException("Vehicle not found")
        );
    }

    @GetMapping("/explore/search")
    public List<VehicleSearchInfo> getAllSearchVehicles(
            Authentication authentication,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @ModelAttribute VehicleFilter vehicleFilters
    ){
        List<VehicleSearchInfo> vehicles;

        if(authentication != null && authentication.isAuthenticated()){
             vehicles = vehicleService.findVehicleSearchSetByDifferentOwner(
                    customUserDetails.getId(), vehicleFilters
            );
        }else{
            vehicles = vehicleService.findVehicleSearchSetByDifferentOwner(null, vehicleFilters);
        }

        return vehicles;
    }

    @GetMapping("/edit-form/{vehicleId}/fetch-vehicle-images")
    public List<VehicleImage> fetchVehicleImagesForEditForm(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long vehicleId
    ){
        return vehicleService.fetchVehicleImagesForEditForm(
                customUserDetails.getId(), vehicleId
        );
    }

    @GetMapping("/all-fleet")
    public PageResponse<VehicleFleetDto> getAllFleetVehicles(
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
                        null,
                        pageable
                );

        return new PageResponse<>(vehiclePage);
    }

    /*--------------------------------------------POST--------------------------------------------------- */

    @Operation(  // Swagger/OpenAPI 3.x annotation to describe the endpoint
            summary = "Small summary of the end-point",
            description = "A detailed description of the end-point"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RegisterVehicleResponseDTO> registerVehicle(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute VehicleRegistrationDTO dto,
            @Parameter(
                    description = "Vehicle images files to be uploaded",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            )
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
    public ResponseEntity<Void> changeVehicleStatus(
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
    public ResponseEntity<Integer> updateVehicle(
            @PathVariable Long vehicleId,
            @RequestBody VehicleUpdateFormData dto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(vehicleService.updateVehiclePartial(vehicleId, dto));
    }

    /*--------------------------------------------DELETE--------------------------------------------------- */

    @DeleteMapping
    public ResponseEntity<Integer> deleteVehicleFromFleet(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long vehicleId
    ){
        int val = vehicleService.deleteVehicleById(vehicleId, customUserDetails.getId());
        return ResponseEntity.ok(val);
    }


}
