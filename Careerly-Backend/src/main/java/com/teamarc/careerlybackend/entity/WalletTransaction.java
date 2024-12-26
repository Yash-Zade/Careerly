package com.teamarc.careerlybackend.entity;

import com.teamarc.careerlybackend.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @OneToOne
    @JoinColumn(name = "session_id")
    private Session session;

    private String transactionId;

    @ManyToOne
    private Wallet wallet;

    @CurrentTimestamp
    private LocalDateTime timeStamp;
}
