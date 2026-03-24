package org.intech.vehiclerental.dto.requestbody;

import org.intech.vehiclerental.models.enums.VehicleStatus;

public record VehicleStatusUpdateRequest(
        VehicleStatus status,
        Long vehicleId
) {}