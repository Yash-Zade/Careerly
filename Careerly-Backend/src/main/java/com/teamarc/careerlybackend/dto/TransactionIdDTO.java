package com.teamarc.careerlybackend.dto;

import lombok.Data;

@Data
public class TransactionIdDTO {
    private Long UserTransactionId;
    private Long AdminTransactionId;
    private Long MentorTransactionId;
}
