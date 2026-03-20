package org.intech.vehiclerental.repositories.datajpa;

import com.blazebit.persistence.spring.data.repository.EntityViewRepository;
import org.intech.vehiclerental.dto.vehicledto.VehicleInfo;
import org.intech.vehiclerental.dto.vehicledto.VehicleWithAccountOwnerView;
import org.intech.vehiclerental.models.Vehicle;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends EntityViewRepository<Vehicle, Long> {

//    Optional<Vehicle> findVehicleById(Long vehicleId);
//    Optional<VehicleInfo> findVehicleInfoViewById(Long vehicleId);
//    Optional<VehicleWithAccountOwnerView> findVehicleWithAccountOwnerViewById(Long vehicleId);

    Optional<Vehicle> findEntityById(Long id);
    Optional<VehicleInfo> findInfoViewById(Long id);
    Optional<VehicleWithAccountOwnerView> findWithOwnerViewById(Long id);
}
