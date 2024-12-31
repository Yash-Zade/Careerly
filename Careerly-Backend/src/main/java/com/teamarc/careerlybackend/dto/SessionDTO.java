package com.teamarc.careerlybackend.dto;

import com.teamarc.careerlybackend.entity.enums.SessionType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SessionDTO {

    private Long sessionId;

    @NotEmpty(message = "Session start time cannot be empty")
    private String sessionStartTime;

    @NotEmpty(message = "Session end time cannot be empty")
    private String sessionEndTime;

    private SessionType sessionType;

    @Positive(message = "Session fee must be a positive value")
    private BigDecimal sessionFee;

    private RatingDTO rating;

    private Long mentorId;

    private Long applicantId;

    private String sessionLink;

}

