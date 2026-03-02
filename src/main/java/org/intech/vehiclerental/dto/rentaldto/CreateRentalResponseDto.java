package org.intech.vehiclerental.dto.rentaldto;

import org.intech.vehiclerental.models.enums.RentalStatus;

import java.time.Instant;

public record CreateRentalResponseDto(
    Long id,
    RentalStatus status,
    Double totalAmount,
    Instant createdAt
) {
}
