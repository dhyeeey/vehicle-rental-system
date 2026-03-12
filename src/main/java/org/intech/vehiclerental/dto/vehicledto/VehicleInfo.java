package org.intech.vehiclerental.dto.vehicledto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.FuelType;
import org.intech.vehiclerental.models.enums.TransmissionType;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.intech.vehiclerental.models.enums.VehicleType;

import java.time.Instant;
import java.util.Set;

/**
 * Projection for {@link org.intech.vehiclerental.models.Vehicle}
 */
@EntityView(Vehicle.class)
public interface VehicleInfo {

    @IdMapping
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

    Double getPricePerDay();

    VehicleStatus getStatus();

    String getDescription();

    String getLocation();

    Boolean getIsAvailable();

    Instant getCreatedAt();

    Set<String> getFeatures();

    Set<VehicleImageView> getImages();
}