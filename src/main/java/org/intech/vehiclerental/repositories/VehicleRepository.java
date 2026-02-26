package org.intech.vehiclerental.repositories;

import org.intech.vehiclerental.dto.vehicledto.InterfaceVehicleInfo;
import org.intech.vehiclerental.dto.vehicledto.VehicleDetailsRecord;
import org.intech.vehiclerental.dto.vehicledto.VehicleFleetDTO;
import org.intech.vehiclerental.dto.vehicledto.VehicleSearchInfo;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

//    @Query("select v from Vehicle v JOIN FETCH v.images where v.accountOwner = ?1 and v.status = ?2 and v.isAvailable = ?3")
    @EntityGraph(attributePaths = {"images"})
    Page<VehicleFleetDTO> findByAccountOwnerAndStatusAndIsAvailable(
                AccountOwner accountOwner,
                VehicleStatus status,
                Boolean isAvailable,
                Pageable pageable
        );

//    @Query("select v from Vehicle v JOIN FETCH v.images vi where v.id = ?1")
    @EntityGraph(attributePaths = {"images", "features"})
    Optional<InterfaceVehicleInfo> findProjectedById(Long id);

//    @Query("""
//        SELECT new org.intech.vehiclerental.dto.vehicledto.VehicleDetailsRecord(
//            v.id, v.registrationNumber, v.vin, v.make, v.model, v.year,
//            v.color, v.type, v.fuelType, v.transmissionType, v.seatingCapacity,
//            v.mileage, v.pricePerDay, v.status, v.description, v.location,
//            v.isAvailable, v.createdAt, v.updatedAt,
//            v.averageRating, v.reviewCount
//        )
//        FROM Vehicle v
//        WHERE v.id = :id
//    """)
//    Optional<VehicleDetailsRecord> findVehicleRecordById(@Param("id") Long id);

    @Query("SELECT v FROM Vehicle v JOIN FETCH v.accountOwner ao")
    List<Vehicle> findAllWithOwner(Pageable pageable);

    @EntityGraph(attributePaths = {"images"})
    @Query("select v from Vehicle v where v.accountOwner <> ?1")
    Set<VehicleSearchInfo> findByAccountOwnerNot(AccountOwner accountOwner);

    @Query("SELECT v FROM Vehicle v JOIN FETCH v.accountOwner ao WHERE v.id = :id")
    Optional<Vehicle> findByIdWithOwner(Long id);
}
