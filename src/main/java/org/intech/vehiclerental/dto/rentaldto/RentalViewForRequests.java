package org.intech.vehiclerental.dto.rentaldto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import org.intech.vehiclerental.models.Rental;
import org.intech.vehiclerental.models.enums.RentalStatus;

import java.time.Instant;

@EntityView(Rental.class)
public interface RentalViewForRequests {
    @IdMapping
    Long getId();

    @Mapping("renter")
    UserViewForRentalRequest getRenter();

    @Mapping("actualStartDateTime")
    Instant getActualStartDateTime();

    @Mapping("actualEndDateTime")
    Instant getActualEndDateTime();

    @Mapping("scheduledStartDateTime")
    Instant getScheduledStartDateTime();

    @Mapping("scheduledEndDateTime")
    Instant getScheduledEndDateTime();

    Double getTotalAmount();

    RentalStatus getStatus();

    String getSpecialInstructions();

    Instant getCreatedAt();
}