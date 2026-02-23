package org.intech.vehiclerental.dto;

import org.intech.vehiclerental.models.VehicleImage;
import org.intech.vehiclerental.models.enums.TransmissionType;

import java.util.List;
import java.util.Set;

/**
 * Projection for {@link org.intech.vehiclerental.models.Vehicle}
 */
public interface VehicleInfo {
    Long getId();

    String getMake();

    Set<ImageView> getImages();

    String getModel();

    Integer getYear();

    TransmissionType getTransmissionType();

    Integer getSeatingCapacity();

    Long getPricePerDay();

    public interface ImageView {
        String getImageUrl();
        Boolean getIsPrimary();
    }
}