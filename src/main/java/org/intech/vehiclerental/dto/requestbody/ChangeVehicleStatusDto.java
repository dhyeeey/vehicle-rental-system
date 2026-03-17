package org.intech.vehiclerental.dto.requestbody;

import org.intech.vehiclerental.models.enums.VehicleApprovalStatus;
import org.intech.vehiclerental.models.enums.VehicleStatus;

public record ChangeVehicleStatusDto(
        Long vehicleId,
         VehicleStatus vehicleStatus,
         VehicleApprovalStatus vehicleApprovalStatus
) {
}
