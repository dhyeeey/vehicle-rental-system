package org.intech.vehiclerental.dto.rentaldto;

public record ReviewData(
        Boolean hasReview,
        ExistingReviewView review
) {
}
