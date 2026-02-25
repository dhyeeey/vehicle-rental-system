package org.intech.vehiclerental.dto.vehicledto;

import org.intech.vehiclerental.models.enums.FuelType;
import org.intech.vehiclerental.models.enums.TransmissionType;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.intech.vehiclerental.models.enums.VehicleType;

import java.time.Instant;
import java.util.Set;

/**
 * Projection for {@link org.intech.vehiclerental.models.Vehicle}
 */
public interface InterfaceVehicleInfo {
    Long getId();

    String getRegistrationNumber();

    String getVin();

    String getMake();

    String getModel();

    Integer getYear();

    String getColor();

    VehicleType getType();

    FuelType getFuelType();

    TransmissionType getTransmissionType();

    Integer getSeatingCapacity();

    Integer getMileage();

    Long getPricePerDay();

    VehicleStatus getStatus();

    String getDescription();

    String getLocation();

    Boolean getIsAvailable();

    Instant getCreatedAt();

    Set<String> getFeatures();

    Set<InterFaceVehicleImageInfo> getImages();

    /**
     * Projection for {@link org.intech.vehiclerental.models.VehicleImage}
     */
    interface InterFaceVehicleImageInfo {
        Long getId();

        String getImageUrl();

        Integer getDisplayOrder();

        Boolean getIsPrimary();

        String getCaption();

        Instant getCreatedAt();
    }
}