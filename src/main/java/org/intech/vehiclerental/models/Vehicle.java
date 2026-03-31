package org.intech.vehiclerental.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.intech.vehiclerental.models.enums.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vehicles", indexes = {
        @Index(name = "idx_vehicle_registration", columnList = "registrationNumber"),
        @Index(name = "idx_vehicle_id", columnList = "id"),
        @Index(name = "idx_vehicle_vin", columnList = "vin")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String registrationNumber;

    @Column(nullable = false, unique = true, length = 50)
    private String vin;

    @Column(nullable = false, length = 100)
    private String make;

    @Column(nullable = false, length = 100)
    private String model;

    @Column(nullable = false)
    private Integer year;

    @Column(length = 50, nullable = false)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VehicleType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransmissionType transmissionType;

    @Column(length = 500)
    private String pickupLocation;

    @Column(length = 500)
    private String dropoffLocation;

    @Column(nullable = false)
    private Integer seatingCapacity;

    @Column(nullable = false)
    private Integer mileage;

    @Column(nullable = false)
    private Double pricePerDay;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_owner_id", nullable = false)
    private AccountOwner accountOwner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VehicleStatus status;

    @Column(length = 500)
    private String description;

    @Column(length = 255)
    private String location;

    @Column(nullable = false)
    private Boolean isAvailable;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Company approvedBy;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private Instant approvedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 20)
    private VehicleApprovalStatus approvalStatus;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private Instant updatedAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Rental> rentals = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<VehicleImage> images = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "vehicle_features",
            joinColumns = @JoinColumn(name = "vehicle_id")
    )
    @Column(name = "feature", length = 100)
    private Set<String> features = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Review> reviews = new HashSet<>();

    @Formula("(select avg(r.rating) from reviews r where r.vehicle_id = id)")
    private Double averageRating;

    @Formula("(select count(r.id) from reviews r where r.vehicle_id = id)")
    private Integer reviewCount;

    @PrePersist
    protected void onCreate() {
        if (isAvailable == null) {
            isAvailable = true;
        }
        if(approvalStatus == null){
            approvalStatus = VehicleApprovalStatus.PENDING;
        }
        if (status == null) {
            status = VehicleStatus.INACTIVE;
        }
    }
}