package com.teamarc.careerlybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EmailRequest {
    private String toEmail;
    private String subject;
    private String body;
    private String buttonText;
    private String buttonUrl;
}
