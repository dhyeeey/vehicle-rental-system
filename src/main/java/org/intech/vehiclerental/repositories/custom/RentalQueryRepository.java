package org.intech.vehiclerental.repositories.custom;

import com.blazebit.persistence.PagedList;
import org.intech.vehiclerental.dto.rentaldto.ExistingReviewView;
import org.intech.vehiclerental.dto.rentaldto.RentalInfo;
import org.intech.vehiclerental.dto.rentaldto.RentalListDto;
import org.intech.vehiclerental.dto.rentaldto.RentalViewForRequests;
import org.intech.vehiclerental.dto.requestbody.SubmitReviewPayload;
import org.intech.vehiclerental.models.Rental;
import org.intech.vehiclerental.models.User;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.RentalStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RentalQueryRepository {

    Optional<RentalInfo> findRentalInfoById(Long id);

    PagedList<RentalListDto> findRentalPageByRenter(
            User renter,
            RentalStatus status,
            Pageable pageable
    );

    List<RentalViewForRequests> findRentalRequestsByVehicleId(Long vehicleId, Long userId);

    PagedList<RentalListDto> findRentalPageByVehicle(
            Vehicle vehicle,
            RentalStatus status,
            Pageable pageable
    );

    void addRentalReview(SubmitReviewPayload payload, Long userId);

    ExistingReviewView fetchExistingReviewOfRental(Long rentalId, Long userId);

    int changeRentalStatus(Long rentalId, RentalStatus rentalStatus, Long userId);

    Boolean isCarOwnerAndLoggedUserSame(Long loggedUserId, Long rentalId);

    Rental saveRental(Rental rental);

    void deleteRentalById(Long id);
}