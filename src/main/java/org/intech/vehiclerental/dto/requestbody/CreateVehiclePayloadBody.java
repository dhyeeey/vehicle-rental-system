package org.intech.vehiclerental.dto.requestbody;

import org.intech.vehiclerental.models.enums.FuelType;
import org.intech.vehiclerental.models.enums.TransmissionType;
import org.intech.vehiclerental.models.enums.VehicleType;

public record CreateVehiclePayloadBody(
        String registrationNumber,

        String vin,

        String make,

        String model,

        Integer year,

        String color,

        VehicleType type,

        FuelType fuelType,

        TransmissionType transmissionType,

        Integer seatingCapacity,

        Integer mileage,

        Long pricePerDay,

        Long pricePerHour,

        String description,

        String location
) {
}
