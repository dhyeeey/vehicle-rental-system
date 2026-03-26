package org.intech.vehiclerental.repositories.datajpa;

import com.blazebit.persistence.spring.data.repository.EntityViewRepository;
import org.intech.vehiclerental.dto.vehicledto.VehicleInfo;
import org.intech.vehiclerental.dto.vehicledto.VehicleWithAccountOwnerView;
import org.intech.vehiclerental.models.Vehicle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends EntityViewRepository<Vehicle, Long> {

//    Optional<Vehicle> findVehicleById(Long vehicleId);
//    Optional<VehicleInfo> findVehicleInfoViewById(Long vehicleId);
//    Optional<VehicleWithAccountOwnerView> findVehicleWithAccountOwnerViewById(Long vehicleId);

    @Query("SELECT v FROM Vehicle v WHERE v.id = :vehicleId")
    Optional<Vehicle> findEntityById(@Param("vehicleId") Long vehicleId);
    Optional<VehicleInfo> findInfoViewById(Long id);
    Optional<VehicleWithAccountOwnerView> findWithOwnerViewById(Long id);

    @Query("SELECT r.vehicle FROM Rental r WHERE r.id = :rentalId")
    Optional<Vehicle> findVehicleByRentalId(@Param("rentalId") Long rentalId);
}
