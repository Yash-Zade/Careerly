package com.teamarc.careerlybackend.dto;

import com.teamarc.careerlybackend.entity.Session;
import com.teamarc.careerlybackend.entity.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class WalletTransactionDto {
    private Long id;
    private BigDecimal amount;
    private TransactionType transactionType;
    private Session session;
    private String transactionId;
    private WalletDTO wallet;
    private LocalDateTime timeStamp;
}
