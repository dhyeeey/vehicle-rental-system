package org.intech.vehiclerental.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
public class Company extends AccountOwner {

    @Column(nullable = false, unique = true, length = 200)
    private String name;

    @Column(unique = true, length = 50)
    private String registrationNumber;

    @Column(unique = true, length = 50)
    private String taxId;

    @JsonManagedReference
    @OneToMany(mappedBy = "approvedBy")
    private Set<Vehicle> approvedVehicles = new HashSet<>();

    @Builder
    public Company(String name, String registrationNumber,
                   String taxId) {
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.taxId = taxId;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        Company other = (Company) obj;

        return getId() != null && getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}