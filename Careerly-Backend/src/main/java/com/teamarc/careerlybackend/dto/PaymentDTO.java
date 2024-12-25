package com.teamarc.careerlybackend.dto;

import com.teamarc.careerlybackend.entity.enums.PaymentStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Date;

@Data
public class PaymentDTO {

    private String paymentId;

    @Positive(message = "Amount must be a positive value")
    private Float amount;

    private Date paymentDate;

    private Float adminCut;

    private PaymentStatus status;

    private AdminDTO admin;

    private SessionDTO session;
}
