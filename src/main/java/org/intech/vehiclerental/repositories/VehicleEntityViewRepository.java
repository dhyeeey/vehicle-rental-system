package org.intech.vehiclerental.repositories;

import com.blazebit.persistence.PagedList;
import org.intech.vehiclerental.dto.vehicledto.*;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.VehicleApprovalStatus;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface VehicleEntityViewRepository {

    Optional<VehicleInfo> findVehicleInfoById(Long id);

    PagedList<VehicleFleetDto> findVehicleFleetPageByOwner(
            Long accountOwnerId,
            VehicleStatus status,
            Boolean isAvailable,
            Pageable pageable
    );

    int updateVehicleStatus(Long vehicleId, VehicleStatus status, Long accountOwnerId);

    List<VehicleSearchInfo> findVehicleSearchList(
            String location,
            Long minPrice,
            Long maxPrice,
            Integer minSeats
    );

    Optional<AccountOwner> findVehicleOwnerByVehicleId(Long vehicleId);

    List<VehicleSearchInfo> findVehicleSearchSetByDifferentOwner(Long accountOwnerId);

    Optional<Vehicle> findVehicleEntityWithOwnerById(Long id);

    Vehicle saveVehicle(Vehicle vehicle);

    int deleteVehicleById(Long id, Long accountOwnerId);

    Vehicle findVehicleById(Long vehicleId);

    int changeVehicleApprovalStatus(Long vehicleId,
                            VehicleStatus vehicleStatus,
                            VehicleApprovalStatus vehicleApprovalStatus,
                            Long accountOwnerId);

    PagedList<VehicleListViewAdmin> getVehicleListForAdminAndCompanyByStatus(
            VehicleStatus vehicleStatus,
            VehicleApprovalStatus vehicleApprovalStatus,int page,
            int size
    );

    int updateVehiclePartial(Long vehicleId, VehicleUpdateFormData dto);
}
