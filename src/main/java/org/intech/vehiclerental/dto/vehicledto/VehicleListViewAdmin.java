package org.intech.vehiclerental.dto.vehicledto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import org.intech.vehiclerental.models.*;
import org.intech.vehiclerental.models.enums.*;

import java.time.Instant;
import java.util.Set;

@EntityView(Vehicle.class)
public interface VehicleListViewAdmin {
    @IdMapping
    Long getId();

    String getMake();
    String getModel();
    Integer getYear();
    String getRegistrationNumber();
    String getLocation();
    Double getPricePerDay();
    VehicleStatus getStatus();
    VehicleApprovalStatus getApprovalStatus();
    FuelType getFuelType();
    TransmissionType getTransmissionType();
    Integer getSeatingCapacity();

    @Mapping("images")
    Set<VehicleImageView> getImages();
    AccountOwnerView getApprovedBy();

    @EntityView(AccountOwner.class)
    public interface AccountOwnerView {

        @IdMapping
        Long getId();
        Role getRole();
        String getEmail();
        String getProfileImageUrl();
        String getPhoneNumber();
    }

    @EntityView(Company.class)
    public interface CompanyView extends AccountOwnerView {

        String getName();
        String getRegistrationNumber();
    }

    @EntityView(User.class)
    public interface UserView extends AccountOwnerView {

        String getFirstName();
        String getLastName();
    }

    @EntityView(VehicleImage.class)
    public interface VehicleImageView {
        @IdMapping
        Long getId();

        String getImageUrl();
        Integer getDisplayOrder();
        Boolean getIsPrimary();
        String getCaption();
        Instant getCreatedAt();
    }
}