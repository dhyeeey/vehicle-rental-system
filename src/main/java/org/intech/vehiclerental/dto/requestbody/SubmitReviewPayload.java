package org.intech.vehiclerental.dto.requestbody;

public record SubmitReviewPayload(
        Long rentalId,
        Long vehicleId,
        Float rating,
        String comment
) {
}
