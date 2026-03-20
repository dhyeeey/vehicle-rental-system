package org.intech.vehiclerental.dto.admin;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.enums.AccountStatus;
import org.intech.vehiclerental.models.enums.Role;

import java.time.Instant;

@EntityView(AccountOwner.class)
public interface UserDetailAdminDto {
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

    Instant getCreatedAt();

    Instant getUpdatedAt();

    @Mapping("TREAT(this AS User).firstName")
    String getFirstName();

    @Mapping("TREAT(this AS User).lastName")
    String getLastName();

    @Mapping("TREAT(this AS User).licenseNumber")
    String getLicenseNumber();

    @Mapping("TREAT(this AS User).isVerified")
    Boolean getIsVerified();
}