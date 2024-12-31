package com.teamarc.careerlybackend.dto;

import com.teamarc.careerlybackend.entity.JobApplication;
import jakarta.persistence.ElementCollection;
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

    private List<JobApplication> jobApplications;

    private List<String> skills;

    private List<String> preferredLocations;


}
