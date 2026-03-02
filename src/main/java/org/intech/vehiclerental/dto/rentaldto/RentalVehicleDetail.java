package org.intech.vehiclerental.dto.rentaldto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import org.intech.vehiclerental.models.Vehicle;

@EntityView(Vehicle.class)
public interface RentalVehicleDetail {

    @IdMapping
    Long getId();

    String getMake();
    String getModel();
    String getRegistrationNumber();
    Double getPricePerDay();
}
