package org.intech.vehiclerental.dto.vehicledto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.*;

import java.time.Instant;
import java.util.Set;

@EntityView(Vehicle.class)
public interface VehicleWithAccountOwnerView {
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

    AccountOwnerInVehicleView getAccountOwner();

    VehicleStatus getStatus();

    String getDescription();

    String getLocation();

    Boolean getIsAvailable();

    Instant getApprovedAt();

    VehicleApprovalStatus getApprovalStatus();

    Instant getCreatedAt();

    Instant getUpdatedAt();

    Set<String> getFeatures();

    @EntityView(AccountOwner.class)
    public interface AccountOwnerInVehicleView {
        @IdMapping
        Long getId();

        Role getRole();

        String getEmail();

        String getPassword();

        String getProfileImageUrl();

        AccountStatus getAccountStatus();

        String getPhoneNumber();

        String getAddress();

        String getCity();

        String getState();

        Long getZipCode();

        String getCountry();

        Instant getCreatedAt();

        Instant getUpdatedAt();
    }
}