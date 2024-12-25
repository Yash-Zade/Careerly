package com.teamarc.careerlybackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "wallets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @DecimalMin("0.0")
    private BigDecimal balance = 0.0;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    private Set<Payment> transactions = new HashSet<>();
}
