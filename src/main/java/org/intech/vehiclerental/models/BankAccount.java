package org.intech.vehiclerental.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "bank_accounts", indexes = {
        @Index(name = "idx_bank_account_owner", columnList = "account_owner_id"),
        @Index(name = "idx_bank_account_number", columnList = "accountNumber")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_owner_id", nullable = false)
    private AccountOwner accountOwner;

    @Column(nullable = false, length = 100)
    private String accountHolderName;

    @Column(nullable = false, length = 20)
    private String accountNumber;

    @Column(nullable = false, length = 11)
    private String ifscCode;

    @Column(nullable = false, length = 150)
    private String bankName;

    @Column(length = 200)
    private String branchName;

    @Column(length = 150)
    private String email;

    @Column(length = 15)
    private String phoneNumber;

    @Column(length = 15)
    private String upiId;

    @Column(length = 10)
    private String panNumber;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private Instant updatedAt;

}