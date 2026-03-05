package org.intech.vehiclerental.repositories;

import com.blazebit.persistence.PagedList;
import org.intech.vehiclerental.dto.vehicledto.VehicleFleetDto;
import org.intech.vehiclerental.dto.vehicledto.VehicleInfo;
import org.intech.vehiclerental.dto.vehicledto.VehicleSearchInfo;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface VehicleEntityViewRepository {

    Optional<VehicleInfo> findVehicleInfoById(Long id);

    PagedList<VehicleFleetDto> findVehicleFleetPageByOwner(
            AccountOwner owner,
            VehicleStatus status,
            Boolean isAvailable,
            Pageable pageable
    );

    int updateVehicleStatus(Long vehicleId, VehicleStatus status, AccountOwner accountOwner);

    List<VehicleSearchInfo> findVehicleSearchList(
            String location,
            Long minPrice,
            Long maxPrice,
            Integer minSeats
    );

    Optional<AccountOwner> findVehicleOwnerByVehicleId(Long vehicleId);

    Set<VehicleSearchInfo> findVehicleSearchSetByDifferentOwner(AccountOwner owner);

    Optional<Vehicle> findVehicleEntityWithOwnerById(Long id);

    Vehicle saveVehicle(Vehicle vehicle);

    int deleteVehicleById(Long id, AccountOwner owner);
}
