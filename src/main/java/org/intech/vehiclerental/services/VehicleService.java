package org.intech.vehiclerental.services;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.intech.vehiclerental.dto.vehicledto.InterfaceVehicleInfo;
import org.intech.vehiclerental.dto.vehicledto.VehicleDetailsRecord;
import org.intech.vehiclerental.dto.vehicledto.VehicleFleetDTO;
import org.intech.vehiclerental.dto.requestbody.VehicleRegistrationDTO;
import org.intech.vehiclerental.mappers.VehicleMapper;
import org.intech.vehiclerental.models.*;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.intech.vehiclerental.repositories.VehicleRepository;
import org.intech.vehiclerental.views.VehicleViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper){
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    public AccountOwner getVehicleOwnerByVehicleId(Long vehicleId){
        Vehicle vehicle = vehicleRepository.findByIdWithOwner(vehicleId).get();
        AccountOwner owner = vehicle.getAccountOwner();

        if (owner instanceof User user) {
            System.out.println(user.getFirstName());
        } else if (owner instanceof Company company) {
            System.out.println(company.getName());
        }

        return owner;
    }

    public Page<VehicleFleetDTO> getCurrentAccountFleetVehicles(
            Pageable pageable,
            CustomUserDetails userDetails,
            VehicleStatus vehicleStatus,
            Boolean isAvailable
    ){
        AccountOwner accountOwner = userDetails.getAccountOwner();

        return vehicleRepository.findByAccountOwnerAndStatusAndIsAvailable(
                accountOwner,
                vehicleStatus,
                isAvailable,
                pageable
        );
    }

    public VehicleDetailsRecord findVehicleById(Long vehicleId){
        VehicleDetailsRecord vehicle = vehicleRepository.findVehicleRecordById(vehicleId).orElseThrow(()->new RuntimeException(""));

        return vehicle;
    }

    public Vehicle registerVehicle(@Valid VehicleRegistrationDTO dto,
                                   List<MultipartFile> images,
                                   Integer primaryImageIndex,
                                   AccountOwner accountOwner) {

        Vehicle vehicle = Vehicle.builder()
                .accountOwner(accountOwner)
                .registrationNumber(dto.registrationNumber())
                .vin(dto.vin())
                .make(dto.make())
                .model(dto.model())
                .year(dto.year())
                .color(dto.color())
                .type(dto.type())
                .fuelType(dto.fuelType())
                .transmissionType(dto.transmissionType())
                .seatingCapacity(dto.seatingCapacity())
                .mileage(dto.mileage())
                .pricePerDay(dto.pricePerDay())
                .description(dto.description())
                .location(dto.location())
                .build();

        for (int i = 0; i < images.size(); i++) {

            MultipartFile file = images.get(i);

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            try {
                Path uploadPath = Paths.get("uploads/vehicles/");
                Files.createDirectories(uploadPath);

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store vehicle image", e);
            }

            VehicleImage vehicleImage = VehicleImage.builder()
                    .vehicle(vehicle)
                    .imageUrl("/uploads/vehicles/" + fileName)
                    .displayOrder(i)
                    .isPrimary(i == primaryImageIndex)
                    .caption(null)
                    .build();

            vehicle.getImages().add(vehicleImage);
        }

        return vehicleRepository.save(vehicle);
    }
}
