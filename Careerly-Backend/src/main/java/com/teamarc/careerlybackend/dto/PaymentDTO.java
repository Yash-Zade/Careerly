package com.teamarc.careerlybackend.dto;

import com.teamarc.careerlybackend.entity.enums.PaymentStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PaymentDTO {

    private Long paymentId;

    @Positive(message = "Amount must be a positive value")
    private BigDecimal amount;

    @CurrentTimestamp
    private Date paymentDate;

    private BigDecimal adminCut;

    private PaymentStatus status;

    private AdminDTO admin;

    private SessionDTO session;
}
