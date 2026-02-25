package org.intech.vehiclerental.dto.vehicledto;

import org.intech.vehiclerental.models.VehicleImage;
import org.intech.vehiclerental.models.enums.FuelType;
import org.intech.vehiclerental.models.enums.TransmissionType;
import org.intech.vehiclerental.models.enums.VehicleStatus;
import org.intech.vehiclerental.models.enums.VehicleType;

import java.time.Instant;
import java.util.Set;

public record VehicleDetailsRecord(
        Long id,
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
        VehicleStatus status,
        String description,
        String location,
        Boolean isAvailable,
        Instant createdAt,
        Instant updatedAt,
        Double averageRating,
        Integer reviewCount
) {
}