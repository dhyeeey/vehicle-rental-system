package org.intech.vehiclerental.repositories.datajpa;

import com.blazebit.persistence.spring.data.repository.EntityViewRepository;
import org.intech.vehiclerental.dto.rentaldto.RentalDetailViewForRentalRequest;
import org.intech.vehiclerental.models.Rental;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentalRepository extends EntityViewRepository<Rental, Long> {
    Optional<RentalDetailViewForRentalRequest> findById(Long rentalId);
}
