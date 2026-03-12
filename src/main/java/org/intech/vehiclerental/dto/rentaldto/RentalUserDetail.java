package org.intech.vehiclerental.dto.rentaldto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import org.intech.vehiclerental.models.User;

@EntityView(User.class)
public interface RentalUserDetail {

    @IdMapping
    Long getId();

    String getFirstName();
    String getLastName();
    String getEmail();
}
