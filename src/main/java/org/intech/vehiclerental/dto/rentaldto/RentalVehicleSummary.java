package org.intech.vehiclerental.dto.rentaldto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import org.intech.vehiclerental.dto.vehicledto.VehicleImageView;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.TransmissionType;
import org.intech.vehiclerental.models.enums.VehicleType;

import java.util.List;

@EntityView(Vehicle.class)
public interface RentalVehicleSummary {

    @IdMapping
    Long getId();

    String getMake();
    String getModel();

    String getRegistrationNumber();
    Integer getYear();
    TransmissionType getTransmissionType();
    Integer getSeatingCapacity();
    VehicleType getType();

    @Mapping("images[isPrimary = true]")
    List<VehicleImageView> getImages();

    Double getAverageRating();
    Integer getReviewCount();
}
