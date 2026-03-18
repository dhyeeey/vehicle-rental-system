package org.intech.vehiclerental.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.intech.vehiclerental.models.enums.AccountStatus;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_license_number", columnList = "licenseNumber")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AccountOwner{

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = true, length = 100)
    private String lastName;

    @Column(unique = true, length = 50)
    private String licenseNumber;

    @Column(nullable = true)
    private Boolean isVerified;

    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "renter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Rental> rentals = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (isVerified == null) {
            isVerified = false;
        }
        if (accountStatus == null) {
            accountStatus = AccountStatus.ACTIVE;
        }
    }
}
