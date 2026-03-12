package org.intech.vehiclerental.dto.rentaldto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import org.intech.vehiclerental.models.Rental;
import org.intech.vehiclerental.models.enums.RentalStatus;

import java.time.Instant;

@EntityView(Rental.class)
public interface RentalInfo {

    @IdMapping
    Long getId();

    RentalStatus getStatus();

    Double getBaseAmount();

    Double getTaxAmount();

    Double getDiscountAmount();

    Double getDepositAmount();

    Double getTotalAmount();

    Instant getActualStartDateTime();

    Instant getActualEndDateTime();

    String getPickupLocation();

    String getDropoffLocation();

    Integer getStartMileage();

    Integer getEndMileage();

    String getSpecialInstructions();

    Instant getCreatedAt();

    Instant getUpdatedAt();

    RentalVehicleDetail getVehicle();

    RentalUserDetail getRenter();
}