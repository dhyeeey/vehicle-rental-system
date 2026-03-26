package org.intech.vehiclerental.dto.rentaldto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.Rental;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.*;

import java.time.Instant;
import java.util.Set;

@EntityView(Rental.class)
public interface RentalDetailViewForRentalRequest {
    @IdMapping
    Long getId();

    VehicleDetailViewForRentalRequest getVehicle();

    RenterDetailViewForRentalRequest getRenter();

    Instant getScheduledStartDateTime();

    Instant getScheduledEndDateTime();

    Instant getActualStartDateTime();

    Instant getActualEndDateTime();

    Double getTotalAmount();

    Double getBaseAmount();

    Double getTaxAmount();

    Double getDiscountAmount();

    Double getDepositAmount();

    RentalStatus getStatus();

    String getPickupLocation();

    String getDropoffLocation();

    Integer getStartMileage();

    Integer getEndMileage();

    String getSpecialInstructions();

    Instant getCreatedAt();

    @EntityView(Vehicle.class)
    public interface VehicleDetailViewForRentalRequest {
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

        Byte getQuantity();

        Integer getSeatingCapacity();

        Integer getMileage();

        Double getPricePerDay();

        VehicleStatus getStatus();

        String getDescription();

        String getLocation();

        Boolean getIsAvailable();

        Instant getCreatedAt();

        Set<String> getFeatures();
    }

    @EntityView(AccountOwner.class)
    public interface RenterDetailViewForRentalRequest {
        @IdMapping
        Long getId();

        Role getRole();

        String getEmail();

        String getProfileImageUrl();

        AccountStatus getAccountStatus();

        String getPhoneNumber();

        String getAddress();

        String getCity();

        String getState();

        Long getZipCode();

        String getCountry();

        @Mapping("TREAT(this AS User).firstName")
        String getFirstName();

        @Mapping("TREAT(this AS User).lastName")
        String getLastName();

        @Mapping("TREAT(this AS User).licenseNumber")
        String getLicenseNumber();

        @Mapping("TREAT(this AS User).isVerified")
        Boolean getIsVerified();
    }
}