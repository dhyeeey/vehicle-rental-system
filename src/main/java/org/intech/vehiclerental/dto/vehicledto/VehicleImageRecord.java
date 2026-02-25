package org.intech.vehiclerental.dto.vehicledto;

import java.time.Instant;

public record VehicleImageRecord(
        Long id,
        String imageUrl,
        Integer displayOrder,
        Boolean isPrimary,
        String caption,
        Instant createdAt
) {}