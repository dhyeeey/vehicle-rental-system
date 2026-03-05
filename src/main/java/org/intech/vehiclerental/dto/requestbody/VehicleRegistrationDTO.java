package org.intech.vehiclerental.dto.requestbody;

import jakarta.validation.constraints.*;
import org.intech.vehiclerental.models.enums.FuelType;
import org.intech.vehiclerental.models.enums.TransmissionType;
import org.intech.vehiclerental.models.enums.VehicleType;

import java.util.Set;

public record VehicleRegistrationDTO(

        @NotBlank(message = "Registration number is required")
        @Size(min = 3, max = 50)
        String registrationNumber,

        @NotBlank(message = "VIN is required")
        @Size(min = 17, max = 17, message = "VIN must be exactly 17 characters")
        String vin,

        @NotBlank(message = "Make is required")
        @Size(max = 100)
        String make,

        @NotBlank(message = "Model is required")
        @Size(max = 100)
        String model,

        @NotNull(message = "Year is required")
        @Min(value = 1900, message = "Year must be 1900 or later")
        @Max(value = 2100, message = "Year must be valid")
        Integer year,

        @NotNull(message = "Primary index for image is required")
        Integer primaryImageIndex,

        @NotBlank(message = "Color is required")
        @Size(max = 50)
        String color,

        @NotNull(message = "Vehicle type is required")
        VehicleType type,

        @NotNull(message = "Fuel type is required")
        FuelType fuelType,

        @NotNull(message = "Transmission type is required")
        TransmissionType transmissionType,

        @NotNull(message = "Seating capacity is required")
        @Min(value = 1, message = "Seating capacity must be at least 1")
        Integer seatingCapacity,

        @NotNull(message = "Mileage is required")
        @Min(value = 0, message = "Mileage cannot be negative")
        Integer mileage,

        @NotNull(message = "Price per day is required")
        @DecimalMin(value = "1.00", message = "Price per day must be at least 1")
        Double pricePerDay,

        @DecimalMin(value = "0.00", message = "Price per hour cannot be negative")
        Long pricePerHour,

        @NotBlank(message = "Description is required")
        @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
        String description,

        @NotBlank(message = "Location is required")
        @Size(min = 3, max = 255)
        String location,

        @Size(max = 20, message = "Maximum 20 features allowed")
        Set<String> features
) {
}
