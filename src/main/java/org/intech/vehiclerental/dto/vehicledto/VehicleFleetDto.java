package org.intech.vehiclerental.dto.vehicledto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.TransmissionType;
import org.intech.vehiclerental.models.enums.VehicleStatus;

import java.time.Instant;
import java.util.Set;

/**
 * Projection for {@link org.intech.vehiclerental.models.Vehicle}
 */

@EntityView(Vehicle.class)
public interface VehicleFleetDto {
    @IdMapping
    Long getId();

    String getMake();

    Set<VehicleImageView> getImages();

    String getModel();

    Integer getYear();

    VehicleStatus getStatus();

    String getRegistrationNumber();

    TransmissionType getTransmissionType();

    Integer getSeatingCapacity();

    Double getPricePerDay();
}