package org.intech.vehiclerental.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.intech.vehiclerental.models.enums.Role;
import org.intech.vehiclerental.models.enums.AccountStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account_owners",indexes = {
        @Index(name = "idx_account_owner_id",columnList = "id"),
        @Index(name = "idx_account_owner_email",columnList = "email"),
        @Index(name = "idx_account_owner_phone_number", columnList = "phoneNumber")
})
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class AccountOwner{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

//    @Lob
    @Column(nullable = true, columnDefinition = "TEXT")
    private String profileImageUrl;

//    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 20)
    protected AccountStatus accountStatus;

    @Column(nullable = true, unique = true, length = 20)
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 20)
    private String zipCode;

    @Column(length = 50)
    private String country;

    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "accountOwner",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankAccount> bankAccounts = new ArrayList<>();

    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "accountOwner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicle> vehicles = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private Instant updatedAt;
}