package org.intech.vehiclerental.dto.rentaldto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import org.intech.vehiclerental.models.User;

@EntityView(User.class)
public interface UserViewForRentalRequest {
    @IdMapping
    Long getId();

    String getEmail();

    String getFirstName();

    String getLastName();

    String getPhoneNumber();

    String getLicenseNumber();

    Boolean getIsVerified();
}