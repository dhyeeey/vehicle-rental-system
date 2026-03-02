package org.intech.vehiclerental.dto.rentaldto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import org.intech.vehiclerental.models.Vehicle;

@EntityView(Vehicle.class)
public interface RentalVehicleSummary {

    @IdMapping
    Long getId();

    String getMake();
    String getModel();
}
