package org.intech.vehiclerental.dto.rentaldto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.Rental;
import org.intech.vehiclerental.models.Review;
import org.intech.vehiclerental.models.Vehicle;

import java.time.Instant;

@EntityView(Review.class)
public interface ExistingReviewView {
    @IdMapping
    Long getId();

    Float getRating();

    String getComment();

    @Mapping("vehicle.id")
    Long getVehicle();

    @Mapping("rental.id")
    Long getRental();

    @Mapping("reviewer.id")
    Long getReviewer();

    Instant getCreatedAt();
}