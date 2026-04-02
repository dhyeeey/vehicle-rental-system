package org.intech.vehiclerental.repositories.utility;

import org.intech.vehiclerental.models.Vehicle_;
import org.intech.vehiclerental.models.enums.*;

import java.time.Instant;
import java.util.Set;

/**
 * This class will be used to apply filters at runtime in repository for data filtering
 */

public record VehicleFilter (

    // ===== ENUMS (multi-select supported) =====
    @Filter(field = Vehicle_.STATUS, op = Operator.IN)
    Set<VehicleStatus> status,

    @Filter(field = Vehicle_.APPROVAL_STATUS, op = Operator.IN)
    Set<VehicleApprovalStatus> approvalStatus,

    @Filter(field = Vehicle_.FUEL_TYPE, op = Operator.IN)
    Set<FuelType> fuelType,

    @Filter(field = Vehicle_.TRANSMISSION_TYPE, op = Operator.IN)
    Set<TransmissionType> transmissionType,

    @Filter(field = Vehicle_.TYPE, op = Operator.IN)
    Set<VehicleType> vehicleType,

    @Filter(field = Vehicle_.SEATING_CAPACITY, op = Operator.IN)
    Set<Integer> seats,

    // ===== BOOLEAN =====
    @Filter(field = Vehicle_.IS_AVAILABLE, op = Operator.EQ)
    Boolean isAvailable,


    // ===== TEXT SEARCH =====

    @Filter(field = Vehicle_.MAKE, op = Operator.LIKE)
    String make,

    @Filter(field = Vehicle_.MODEL, op = Operator.LIKE)
    String model,

    @Filter(field = Vehicle_.COLOR, op = Operator.LIKE)
    String color,


    // ===== RANGE FILTERS =====
    @Filter(field = Vehicle_.PRICE_PER_DAY, op = Operator.LE)
    Double perDayMaxPrice,

    @Filter(field = Vehicle_.AVERAGE_RATING, op = Operator.GE)
    Double minRating,

    @Filter(field = Vehicle_.PRICE_PER_DAY, op = Operator.GE)
    Double perDayMinPrice,

//    ========================================================

    @Filter(field = Vehicle_.MILEAGE, op = Operator.LE)
    Integer maxMileage,


    // ===== RELATION FILTERS =====
    @Filter(field = "accountOwner.id", op = Operator.EQ)
    Long ownerId,

    @Filter(field = "approvedBy.id", op = Operator.EQ)
    Long approvedByCompanyId,


    // ===== DATE RANGE =====
    @Filter(field = Vehicle_.CREATED_AT, op = Operator.GE)
    Instant createdAfter,

    @Filter(field = Vehicle_.CREATED_AT, op = Operator.LE)
    Instant createdBefore,

    @Filter(field = Vehicle_.APPROVED_AT, op = Operator.GE)
    Instant approvedAfter,

    @Filter(field = Vehicle_.APPROVED_AT, op = Operator.LE)
    Instant approvedBefore,
    
    @Filter(field = Vehicle_.REVIEW_COUNT, op = Operator.GE)
    Integer minReviews,

    Set<String> features
){}