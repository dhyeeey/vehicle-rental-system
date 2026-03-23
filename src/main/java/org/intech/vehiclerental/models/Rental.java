package org.intech.vehiclerental.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.intech.vehiclerental.models.enums.RentalStatus;

import java.lang.Long;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rentals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant scheduledStartDateTime;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant scheduledEndDateTime;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant actualStartDateTime;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant actualEndDateTime;

    @Column
    private Double totalAmount;

    @Column
    private Double baseAmount;

    @Column
    private Double taxAmount;

    @Column
    private Double discountAmount;

    @Column
    private Double depositAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RentalStatus status;

    @Column(length = 500)
    private String pickupLocation;

    @Column(length = 500)
    private String dropoffLocation;

    @Column
    private Integer startMileage;

    @Column
    private Integer endMileage;

    @Column(length = 1000)
    private String specialInstructions;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private Instant updatedAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Payment> payments = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (status == null) {
            status = RentalStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}