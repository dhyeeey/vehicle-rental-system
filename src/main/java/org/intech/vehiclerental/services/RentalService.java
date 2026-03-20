package org.intech.vehiclerental.services;

import com.blazebit.persistence.PagedList;
import org.intech.vehiclerental.dto.rentaldto.CreateRentalRequestDto;
import org.intech.vehiclerental.dto.rentaldto.RentalInfo;
import org.intech.vehiclerental.dto.rentaldto.RentalListDto;
import org.intech.vehiclerental.dto.rentaldto.RentalViewForRequests;
import org.intech.vehiclerental.models.Rental;
import org.intech.vehiclerental.models.User;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.RentalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RentalService {

    Rental createRental(User renter,
                        CreateRentalRequestDto createRentalRequestDto);

    Optional<RentalInfo> findRentalInfoById(Long id);

    PagedList<RentalListDto> findRentalPageByRenter(
            User renter,
            RentalStatus status,
            Pageable pageable
    );

    int changeRentalStatus(Long rentalId, RentalStatus rentalStatus);

    Boolean isCarOwnerAndLoggedUserSame(Long loggedUserId, Long rentalId);

    List<RentalViewForRequests> findRentalRequestsByVehicleId(Long vehicleId);

    PagedList<RentalListDto> findRentalPageByVehicle(
            Vehicle vehicle,
            RentalStatus status,
            Pageable pageable
    );

    Rental saveRental(Rental rental);

    void deleteRentalById(Long id);
}