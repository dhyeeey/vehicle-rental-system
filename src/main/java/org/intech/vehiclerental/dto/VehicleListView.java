package org.intech.vehiclerental.dto;

import org.intech.vehiclerental.models.enums.VehicleType;

public interface VehicleListView {

    Long getId();
    String getMake();
    String getModel();
    Integer getYear();
    String getLocation();
    Long getPricePerDay();
    Integer getSeatingCapacity();
    VehicleType getType();
    Double getAverageRating();
    Integer getReviewCount();
}