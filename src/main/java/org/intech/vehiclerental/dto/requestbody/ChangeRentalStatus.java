package org.intech.vehiclerental.dto.requestbody;

import org.intech.vehiclerental.models.enums.RentalStatus;

public record ChangeRentalStatus(
        Long rentalId,
        RentalStatus status
) {
}
