package org.intech.vehiclerental.services;

import jakarta.validation.Valid;
import org.intech.vehiclerental.dto.requestbody.VehicleRegistrationDTO;
import org.intech.vehiclerental.dto.vehicledto.VehicleInfo;
import org.intech.vehiclerental.dto.vehicledto.VehicleFleetDto;
import org.intech.vehiclerental.dto.vehicledto.VehicleSearchInfo;
import org.intech.vehiclerental.mappers.VehicleMapper;
import org.intech.vehiclerental.models.*;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.intech.vehiclerental.repositories.VehicleRepository;
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
import java.util.Set;

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

    public Page<VehicleFleetDto> getCurrentAccountFleetVehicles(
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

    public VehicleInfo findVehicleById(Long vehicleId){
        VehicleInfo vehicle = vehicleRepository.findProjectedById(vehicleId).orElseThrow(()->new RuntimeException(""));

        return vehicle;
    }

    public Set<VehicleSearchInfo> findByAccountOwnerNot(AccountOwner accountOwner){
        Set<VehicleSearchInfo> vehicles = vehicleRepository.findByAccountOwnerNot(accountOwner);

        return vehicles;
    }

    /**
     *
     * Currently image uploads are stored in folder locally on temporary basis.
     * Local storing of images will removed in future
     *
     */
    public Vehicle registerVehicle(@Valid VehicleRegistrationDTO dto,
                                   List<MultipartFile> images,
                                   Integer primaryImageIndex,
                                   AccountOwner accountOwner) {

        Vehicle vehicle = vehicleMapper.toVehicleFromVehicleRegistrationDTO(dto);

        vehicle.setAccountOwner(accountOwner);

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
