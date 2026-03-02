package org.intech.vehiclerental.repositories;

import org.intech.vehiclerental.dto.rentaldto.RentalInfo;
import org.intech.vehiclerental.dto.rentaldto.RentalListDto;
import org.intech.vehiclerental.models.Rental;
import org.intech.vehiclerental.models.User;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.RentalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RentalEntityViewRepository {

    Optional<RentalInfo> findRentalInfoById(Long id);

    Page<RentalListDto> findRentalPageByRenter(
            User renter,
            RentalStatus status,
            Pageable pageable
    );

    Page<RentalListDto> findRentalPageByVehicle(
            Vehicle vehicle,
            RentalStatus status,
            Pageable pageable
    );

    Rental saveRental(Rental rental);

    void deleteRentalById(Long id);
}