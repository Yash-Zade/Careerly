package com.teamarc.careerlybackend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SessionDTO {

    private Long sessionId;

    @NotEmpty(message = "Session start time cannot be empty")
    private String sessionStartTime;

    @NotEmpty(message = "Session end time cannot be empty")
    private String sessionEndTime;

    private Boolean isPaid;
    private Boolean isFirstSession;

    @Positive(message = "Session fee must be a positive value")
    private Float sessionFee;

    private RatingDTO rating;

    private PaymentDTO payment;
}
