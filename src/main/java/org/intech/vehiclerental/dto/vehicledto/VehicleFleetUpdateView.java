package org.intech.vehiclerental.dto.vehicledto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.UpdatableEntityView;
import com.blazebit.persistence.view.UpdatableMapping;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.FuelType;
import org.intech.vehiclerental.models.enums.TransmissionType;
import org.intech.vehiclerental.models.enums.VehicleType;

import java.util.Set;

@EntityView(Vehicle.class)
@UpdatableEntityView
public interface VehicleFleetUpdateView {
    @IdMapping
    Long getId();

    void setRegistrationNumber(String registrationNumber);
    String getRegistrationNumber();

    void setVin(String vin);
    String getVin();

    void setMake(String make);
    String getMake();

    void setModel(String model);
    String getModel();

    void setYear(Integer year);
    Integer getYear();

    void setColor(String color);
    String getColor();

    void setType(VehicleType type);
    VehicleType getType();

    void setFuelType(FuelType fuelType);
    FuelType getFuelType();

    void setTransmissionType(TransmissionType transmissionType);
    TransmissionType getTransmissionType();

    void setSeatingCapacity(Integer seatingCapacity);
    Integer getSeatingCapacity();

    void setMileage(Integer mileage);
    Integer getMileage();

    void setPricePerDay(Double pricePerDay);
    Double getPricePerDay();

    void setLocation(String location);
    String getLocation();

    void setDescription(String description);
    String getDescription();

}
