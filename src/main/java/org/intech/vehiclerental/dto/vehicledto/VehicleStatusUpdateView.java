package org.intech.vehiclerental.dto.vehicledto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.persistence.view.UpdatableEntityView;
import org.intech.vehiclerental.models.AccountOwner;
import org.intech.vehiclerental.models.Vehicle;
import org.intech.vehiclerental.models.enums.VehicleApprovalStatus;
import org.intech.vehiclerental.models.enums.VehicleStatus;

@EntityView(Vehicle.class)
@UpdatableEntityView
public interface VehicleStatusUpdateView {

    @IdMapping
    Long getId();

    VehicleStatus getStatus();
    void setStatus(VehicleStatus status);

    VehicleApprovalStatus getApprovalStatus();
    void setApprovalStatus(VehicleApprovalStatus approvalStatus);

    AccountOwnerIdView getApprovedBy();
    void setApprovedBy(AccountOwnerIdView approvedBy);

    @EntityView(AccountOwner.class)
    public interface AccountOwnerIdView {

        @IdMapping
        Long getId();
    }
}