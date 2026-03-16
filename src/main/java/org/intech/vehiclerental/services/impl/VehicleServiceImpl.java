package org.intech.vehiclerental.services.impl;

import com.blazebit.persistence.PagedList;
import jakarta.validation.Valid;
import org.intech.vehiclerental.dto.requestbody.VehicleRegistrationDTO;
import org.intech.vehiclerental.dto.vehicledto.VehicleFleetDto;
import org.intech.vehiclerental.dto.vehicledto.VehicleInfo;
import org.intech.vehiclerental.dto.vehicledto.VehicleListViewAdmin;
import org.intech.vehiclerental.dto.vehicledto.VehicleSearchInfo;
import org.intech.vehiclerental.exceptions.VehicleAccessDeniedException;
import org.intech.vehiclerental.mappers.VehicleMapper;
import org.intech.vehiclerental.models.*;
import org.intech.vehiclerental.models.enums.VehicleApprovalStatus;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.intech.vehiclerental.repositories.VehicleEntityViewRepository;
import org.intech.vehiclerental.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleEntityViewRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Value("${app.upload.vehicles.image.dir}")
    private String vehicleImagesDir;

    @Autowired
    public VehicleServiceImpl(VehicleEntityViewRepository vehicleRepository,
                              VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    /**
     * Here traditional for loop is used instead of enhanced for loop
     * due to need of index for image
     */
    @Override
    @Transactional
    public Vehicle registerVehicle(@Valid VehicleRegistrationDTO dto,
                                   List<MultipartFile> images,
                                   Integer primaryImageIndex,
                                   AccountOwner accountOwner) {

        Vehicle vehicle = vehicleMapper.toVehicleFromVehicleRegistrationDTO(dto);
        vehicle.setStatus(VehicleStatus.INACTIVE);
        vehicle.setApprovalStatus(VehicleApprovalStatus.PENDING);
        vehicle.setAccountOwner(accountOwner);

        for (int i = 0; i < images.size(); i++) {
            MultipartFile file = images.get(i);
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            try {
                Path uploadPath = Paths.get(vehicleImagesDir);
                Files.createDirectories(uploadPath);
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store vehicle image", e);
            }
            VehicleImage vehicleImage = VehicleImage.builder()
                    .vehicle(vehicle)
                    .imageUrl(vehicleImagesDir + fileName)
                    .displayOrder(i).isPrimary(i == primaryImageIndex)
                    .caption(null).build();
            vehicle.getImages().add(vehicleImage);
        }

        return vehicleRepository.saveVehicle(vehicle);
    }

    @Override
    public Optional<AccountOwner> findVehicleOwnerByVehicleId(Long vehicleId) {
        return vehicleRepository.findVehicleOwnerByVehicleId(vehicleId);
    }

    @Override
    public AccountOwner getVehicleOwnerByVehicleIdOrThrow(Long vehicleId) {
        AccountOwner owner = vehicleRepository.findVehicleOwnerByVehicleId(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + vehicleId));

        if (owner instanceof User user) {
            System.out.println(user.getFirstName());
        } else if (owner instanceof Company company) {
            System.out.println(company.getName());
        }

        return owner;
    }

    @Override
    public Optional<VehicleInfo> findVehicleInfoById(Long id) {
        return vehicleRepository.findVehicleInfoById(id);
    }

    @Override
    public PagedList<VehicleFleetDto> findVehicleFleetPageByOwner(AccountOwner owner,
                                                                  org.intech.vehiclerental.models.enums.VehicleStatus status,
                                                                  Boolean isAvailable,
                                                                  Pageable pageable) {
        return vehicleRepository.findVehicleFleetPageByOwner(owner, status, isAvailable, pageable);
    }

    @Override
    public List<VehicleSearchInfo> findVehicleSearchList(String location,
                                                         Long minPrice,
                                                         Long maxPrice,
                                                         Integer minSeats) {
        return vehicleRepository.findVehicleSearchList(location, minPrice, maxPrice, minSeats);
    }

    @Transactional
    public void changeVehicleStatus(Long vehicleId, VehicleStatus status, AccountOwner accountOwner) {

        int updated = vehicleRepository.updateVehicleStatus(vehicleId, status, accountOwner);

        if (updated == 0) {
            throw new VehicleAccessDeniedException("Vehicle not found or you do not own vehicle");
        }
    }

    @Override
    public Set<VehicleSearchInfo> findVehicleSearchSetByDifferentOwner(AccountOwner owner) {
        return vehicleRepository.findVehicleSearchSetByDifferentOwner(owner);
    }

    @Override
    public Optional<Vehicle> findVehicleEntityWithOwnerById(Long id) {
        return vehicleRepository.findVehicleEntityWithOwnerById(id);
    }

    @Override
    @Transactional
    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.saveVehicle(vehicle);
    }

    @Override
    @Transactional
    public int deleteVehicleById(Long id, AccountOwner owner) {
        int deleted = vehicleRepository.deleteVehicleById(id, owner);

        if (deleted == 0) {
            throw new VehicleAccessDeniedException("Vehicle not found or not owned by you");
        }

        return deleted;
    }

    @Override
    @Transactional
    public int changeVehicleApprovalStatus(Long vehicleId,
                                    VehicleStatus vehicleStatus,
                                    VehicleApprovalStatus vehicleApprovalStatus,
                                    AccountOwner accountOwner) {

        int val = vehicleRepository.changeVehicleApprovalStatus(vehicleId, vehicleStatus, vehicleApprovalStatus, accountOwner);
        return val;
    }


    @Override
    public List<VehicleListViewAdmin> getVehicleListForAdminAndCompanyByStatus(VehicleStatus vehicleStatus,
                                                                        VehicleApprovalStatus vehicleApprovalStatus){

        return vehicleRepository.getVehicleListForAdminAndCompanyByStatus(vehicleStatus,vehicleApprovalStatus);
    }


}