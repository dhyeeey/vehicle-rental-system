package org.intech.vehiclerental.repositories.utility;

import org.intech.vehiclerental.models.enums.*;

import java.time.Instant;
import java.util.Set;

/**
 * This class will be used to apply filters at runtime in repository for data filtering
 */

public class VehicleFilter {

    // ===== ENUMS (multi-select supported) =====
    @Filter(field = "status", op = Operator.IN)
    private Set<VehicleStatus> status;

    @Filter(field = "approvalStatus", op = Operator.IN)
    private Set<VehicleApprovalStatus> approvalStatus;

    @Filter(field = "fuelType", op = Operator.IN)
    private Set<FuelType> fuelType;

    @Filter(field = "transmissionType", op = Operator.IN)
    private Set<TransmissionType> transmissionType;

    @Filter(field = "type", op = Operator.IN)
    private Set<VehicleType> vehicleType;


    // ===== BOOLEAN =====
    @Filter(field = "isAvailable", op = Operator.EQ)
    private Boolean isAvailable;


    // ===== TEXT SEARCH =====
    @Filter(field = "location", op = Operator.LIKE)
    private String city;

    @Filter(field = "make", op = Operator.LIKE)
    private String make;

    @Filter(field = "model", op = Operator.LIKE)
    private String model;

    @Filter(field = "color", op = Operator.LIKE)
    private String color;


    // ===== RANGE FILTERS =====
    @Filter(field = "pricePerDay", op = Operator.GE)
    private Double minPrice;

    @Filter(field = "pricePerDay", op = Operator.LE)
    private Double maxPrice;

    @Filter(field = "seatingCapacity", op = Operator.GE)
    private Integer minSeats;

    @Filter(field = "mileage", op = Operator.LE)
    private Integer maxMileage;

    @Filter(field = "year", op = Operator.GE)
    private Integer minYear;

    @Filter(field = "year", op = Operator.LE)
    private Integer maxYear;


    // ===== RELATION FILTERS =====
    @Filter(field = "accountOwner.id", op = Operator.EQ)
    private Long ownerId;

    @Filter(field = "approvedBy.id", op = Operator.EQ)
    private Long approvedByCompanyId;


    // ===== DATE RANGE =====
    @Filter(field = "createdAt", op = Operator.GE)
    private Instant createdAfter;

    @Filter(field = "createdAt", op = Operator.LE)
    private Instant createdBefore;

    @Filter(field = "approvedAt", op = Operator.GE)
    private Instant approvedAfter;

    @Filter(field = "approvedAt", op = Operator.LE)
    private Instant approvedBefore;

    @Filter(field = "averageRating", op = Operator.GE)
    private Double minRating;

    @Filter(field = "reviewCount", op = Operator.GE)
    private Integer minReviews;


    private Set<String> features;
}