package org.intech.vehiclerental.dto.vehicledto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.TransmissionType;
import org.intech.vehiclerental.models.enums.VehicleType;

import java.util.Set;

/**
 * Projection for {@link org.intech.vehiclerental.models.Vehicle}
 */
@EntityView(Vehicle.class)
public interface VehicleSearchInfo {
    @IdMapping
    Long getId();

    String getMake();

    String getModel();

    Integer getYear();

    VehicleType getType();

    TransmissionType getTransmissionType();

    Integer getSeatingCapacity();

    Double getPricePerDay();

    String getLocation();

    Set<VehicleImageView> getImages();
}