package org.intech.vehiclerental.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company extends AccountOwner {

    @Column(nullable = false, unique = true, length = 200)
    private String name;

    @Column(unique = true, length = 50)
    private String registrationNumber;

    @Column(unique = true, length = 50)
    private String taxId;

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

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 150)
    private String email;

    @Builder
    public Company(Long id, List<BankAccount> bankAccounts, List<Vehicle> vehicles,
                   Instant createdAt, Instant updatedAt, String name, String registrationNumber,
                   String taxId, String address, String city, String state, String zipCode,
                   String country, String phoneNumber, String email) {
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.taxId = taxId;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}