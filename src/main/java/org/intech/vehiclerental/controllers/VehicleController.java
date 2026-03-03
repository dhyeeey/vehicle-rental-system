package org.intech.vehiclerental.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.intech.vehiclerental.dto.paginationdto.PageResponse;
import org.intech.vehiclerental.dto.vehicledto.VehicleFleetDTO;
import org.intech.vehiclerental.dto.requestbody.VehicleRegistrationDTO;
import org.intech.vehiclerental.exceptions.InvalidPrimaryIndexOfImage;
import org.intech.vehiclerental.exceptions.NoImageFoundException;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.CustomUserDetails;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.intech.vehiclerental.services.AccountOwnerService;
import org.intech.vehiclerental.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicle")
@Slf4j
public class VehicleController {

    private final VehicleService vehicleService;
    private final AccountOwnerService accountOwnerService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Autowired
    public VehicleController(VehicleService vehicleService, AccountOwnerService accountOwnerService){
        this.vehicleService = vehicleService;
        this.accountOwnerService = accountOwnerService;
    }

    @GetMapping("/getallfleetvehicles")
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

        Page<VehicleFleetDTO> vehiclePage = vehicleService.getCurrentAccountFleetVehicles(
                pageable,
                userDetails,
                VehicleStatus.ACTIVE,
                true
        );

        return ResponseEntity.ok(new PageResponse<>(vehiclePage));
    }


    /**
     *
     * Currently image uploads are stored in folder locally on temporary basis.
     * Local storing of images will removed in future
     *
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> registerVehicle(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute VehicleRegistrationDTO dto,
            @RequestParam(value = "images", required = true) List<MultipartFile> images,
            @RequestParam(value = "primaryImageIndex", defaultValue = "0") Integer primaryImageIndex
    ) {
        AccountOwner accountOwner = userDetails.getAccountOwner();

        log.info("Received vehicle registration request");
        log.info("DTO: {}", dto);
        log.info("Number of images: {}", images.size());
        log.info("Primary image index: {}", primaryImageIndex);

        if (images.isEmpty()) {
            throw new NoImageFoundException("At least one vehicle image is required");
        }

        if (primaryImageIndex < 0 || primaryImageIndex >= images.size()) {
            throw new InvalidPrimaryIndexOfImage("Invalid primary image index");
        }

        for (MultipartFile image : images) {
            if (image.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Empty image file detected");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            if (image.getSize() > 5 * 1024 * 1024) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Image size must not exceed 5MB: " + image.getOriginalFilename());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Invalid file type. Only images are allowed: " + image.getOriginalFilename());
                return ResponseEntity.badRequest().body(errorResponse);
            }
        }

        Vehicle vehicle = vehicleService.registerVehicle(dto, images, primaryImageIndex, accountOwner);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Vehicle registered successfully");
        response.put("vehicleId", vehicle.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> registerVehicleAlternative(
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
