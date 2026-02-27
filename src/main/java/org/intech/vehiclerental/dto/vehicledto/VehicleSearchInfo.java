package org.intech.vehiclerental.dto.vehicledto;

import org.intech.vehiclerental.models.enums.TransmissionType;
import org.intech.vehiclerental.models.enums.VehicleType;

import java.util.Set;

/**
 * Projection for {@link org.intech.vehiclerental.models.Vehicle}
 */
public interface VehicleSearchInfo {
    Long getId();

    String getMake();

    String getModel();

    Integer getYear();

    VehicleType getType();

    TransmissionType getTransmissionType();

    Integer getSeatingCapacity();

    Long getPricePerDay();

    String getLocation();

    Set<VehicleImageView> getImages();
}