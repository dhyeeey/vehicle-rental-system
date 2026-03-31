package org.intech.vehiclerental.repositories.custom;

import com.blazebit.persistence.PagedList;
import org.intech.vehiclerental.dto.vehicledto.VehicleFleetDto;
import org.intech.vehiclerental.dto.vehicledto.VehicleListViewAdmin;
import org.intech.vehiclerental.dto.vehicledto.VehicleSearchInfo;
import org.intech.vehiclerental.dto.vehicledto.VehicleUpdateFormData;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.VehicleApprovalStatus;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.intech.vehiclerental.repositories.utility.VehicleFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VehicleQueryRepository {

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

    List<VehicleSearchInfo> findVehicleSearchSetByDifferentOwner(Long accountOwnerId, VehicleFilter vehicleFilters);

    Vehicle saveVehicle(Vehicle vehicle);

    int deleteVehicleById(Long vehicleId, Long accountOwnerId);

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
