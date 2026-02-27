package org.intech.vehiclerental.dto.vehicledto;

import org.intech.vehiclerental.models.enums.TransmissionType;
import org.intech.vehiclerental.models.enums.VehicleStatus;

import java.time.Instant;
import java.util.Set;

/**
 * Projection for {@link org.intech.vehiclerental.models.Vehicle}
 */
public interface VehicleFleetDto {
    Long getId();

    String getMake();

    Set<VehicleImageView> getImages();

    String getModel();

    Integer getYear();

    VehicleStatus getStatus();

    String getRegistrationNumber();

    TransmissionType getTransmissionType();

    Integer getSeatingCapacity();

    Long getPricePerDay();
}