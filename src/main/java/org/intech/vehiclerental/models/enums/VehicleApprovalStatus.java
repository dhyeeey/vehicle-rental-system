package org.intech.vehiclerental.models.enums;

public enum VehicleApprovalStatus {
    PENDING,        // Submitted by owner
    UNDER_REVIEW,   // Company/Admin reviewing vehicle
    APPROVED,       // Approved for listing
    REJECTED        // Rejected after review
}