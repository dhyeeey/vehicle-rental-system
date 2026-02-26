package org.intech.vehiclerental.dto.vehicledto;

public record RegisterVehicleResponseDTO(
        boolean success,
        String message,
        Long vehicleId
) {
}
