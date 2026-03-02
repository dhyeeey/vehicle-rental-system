package org.intech.vehiclerental.dto.rentaldto;

import java.time.Instant;

public record CreateRentalRequestDto(
        Long vehicleId,
        Instant startDate,
        Instant endDate
) {
}
