package org.intech.vehiclerental.repositories;

import org.intech.vehiclerental.entities.Vehicle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("SELECT v FROM Vehicle v JOIN FETCH v.accountOwner ao")
    List<Vehicle> findAllWithOwner();

    @Query("SELECT v FROM Vehicle v JOIN FETCH v.accountOwner ao")
    List<Vehicle> findAllWithOwner(Pageable pageable);

    @Query("SELECT v FROM Vehicle v JOIN FETCH v.accountOwner ao WHERE v.id = :id")
    Optional<Vehicle> findByIdWithOwner(Long id);

    @Query("SELECT v FROM Vehicle v JOIN FETCH v.accountOwner ao WHERE TYPE(ao) = User")
    List<Vehicle> findVehiclesOwnedByUsers();

    @Query("SELECT v FROM Vehicle v JOIN FETCH v.accountOwner ao WHERE TYPE(ao) = Company")
    List<Vehicle> findVehiclesOwnedByCompanies();

}
