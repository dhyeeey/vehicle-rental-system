package org.intech.vehiclerental.dto.rentaldto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import org.intech.vehiclerental.models.Rental;
import org.intech.vehiclerental.models.enums.RentalStatus;

import java.time.Instant;

@EntityView(Rental.class)
public interface RentalViewForRequests {
    @IdMapping
    Long getId();

    UserViewForRentalRequest getRenter();

    Instant getActualStartDateTime();

    Instant getActualEndDateTime();

    Double getTotalAmount();

    RentalStatus getStatus();

    String getSpecialInstructions();

    Instant getCreatedAt();
}