package org.intech.vehiclerental.services;

import com.blazebit.persistence.PagedList;
import jakarta.validation.Valid;
import org.intech.vehiclerental.dto.requestbody.VehicleRegistrationDTO;
import org.intech.vehiclerental.dto.vehicledto.*;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.VehicleApprovalStatus;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface VehicleService {

    // Vehicle Registration
    Vehicle registerVehicle(@Valid VehicleRegistrationDTO dto,
                            List<MultipartFile> images,
                            Integer primaryImageIndex,
                            Long accountOwnerId);

    // Vehicle Owner
    Optional<AccountOwner> findVehicleOwnerByVehicleId(Long vehicleId);

    AccountOwner getVehicleOwnerByVehicleIdOrThrow(Long vehicleId);

    void changeVehicleStatus(Long vehicleId, VehicleStatus status, Long accountOwnerId);

    // Vehicle Info Projections
    Optional<VehicleInfo> findVehicleInfoById(Long id);

    // Vehicle Fleet Pagination
    PagedList<VehicleFleetDto> findVehicleFleetPageByOwner(
            Long accountOwnerId,
            VehicleStatus status,
            Boolean isAvailable,
            Pageable pageable
    );

    // Vehicle Search
    List<VehicleSearchInfo> findVehicleSearchList(String location,
                                                  Long minPrice,
                                                  Long maxPrice,
                                                  Integer minSeats);

    List<VehicleSearchInfo> findVehicleSearchSetByDifferentOwner(Long accountOwnerId);

    // Entity Operations
    Optional<Vehicle> findVehicleEntityWithOwnerById(Long id);

    Vehicle saveVehicle(Vehicle vehicle);

    int deleteVehicleById(Long id, Long accountOwnerId);

    int changeVehicleApprovalStatus(Long vehicleId, VehicleStatus vehicleStatus,
                             VehicleApprovalStatus vehicleApprovalStatus, Long accountOwnerId);

    int updateVehiclePartial(Long vehicleId, VehicleUpdateFormData dto);

    PagedList<VehicleListViewAdmin> getVehicleListForAdminAndCompanyByStatus(VehicleStatus vehicleStatus,
                                                                             VehicleApprovalStatus vehicleApprovalStatus, int page, int size);
}