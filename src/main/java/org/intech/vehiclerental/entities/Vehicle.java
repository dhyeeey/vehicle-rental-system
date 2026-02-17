package org.intech.vehiclerental.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.intech.vehiclerental.entities.enums.FuelType;
import org.intech.vehiclerental.entities.enums.TransmissionType;
import org.intech.vehiclerental.entities.enums.VehicleStatus;
import org.intech.vehiclerental.entities.enums.VehicleType;

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

    @Column(nullable = false)
    private Integer seatingCapacity;

    @Column(nullable = false)
    private Integer mileage;

    @Column(nullable = false)
    private Long pricePerDay;

    @Column
    private Long pricePerHour;

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

    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private Instant updatedAt;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Rental> rentals = new HashSet<>();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<VehicleImage> images = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (isAvailable == null) {
            isAvailable = true;
        }
        if (status == null) {
            status = VehicleStatus.ACTIVE;
        }
    }
}