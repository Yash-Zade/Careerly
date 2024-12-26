package com.teamarc.careerlybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantDTO {

    private Long applicantId;

    private String resume;

    private UserDTO user;

    private List<JobDTO> appliedJobs;
}
