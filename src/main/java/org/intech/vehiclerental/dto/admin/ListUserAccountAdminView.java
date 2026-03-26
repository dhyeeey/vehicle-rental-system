package org.intech.vehiclerental.dto.admin;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.enums.AccountStatus;
import org.intech.vehiclerental.models.enums.Role;

import java.time.Instant;

@EntityView(AccountOwner.class)
public interface ListUserAccountAdminView {

    @IdMapping
    Long getId();

    Role getRole();

    String getEmail();

    String getProfileImageUrl();

    AccountStatus getAccountStatus();

    String getPhoneNumber();

    String getCity();

    String getState();

    Instant getCreatedAt();

    @Mapping("TREAT(this AS User).firstName")
    String getFirstName();

    @Mapping("TREAT(this AS User).lastName")
    String getLastName();
}