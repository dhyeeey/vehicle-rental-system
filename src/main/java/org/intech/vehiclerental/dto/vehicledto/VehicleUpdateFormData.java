package org.intech.vehiclerental.dto.vehicledto;

import org.intech.vehiclerental.models.enums.FuelType;
import org.intech.vehiclerental.models.enums.TransmissionType;
import org.intech.vehiclerental.models.enums.VehicleType;

import java.util.Set;

public record VehicleUpdateFormData(

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
        Double pricePerDay,

        String location,
        String description,

        String pickupLocation,
        String dropoffLocation,

        Set<String> features

) {}