package com.teamarc.careerlybackend.entity;

import com.teamarc.careerly.entities.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @DecimalMin("0.0")
    private Double amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @NotBlank
    private String description;


    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;


}
