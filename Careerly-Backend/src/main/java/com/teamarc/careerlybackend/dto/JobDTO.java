package com.teamarc.careerlybackend.dto;

import com.teamarc.careerlybackend.entity.enums.JobStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class JobDTO {

    private String jobId;

    @NotEmpty(message = "Job title cannot be empty")
    private String title;

    private String description;

    @NotEmpty(message = "Job location cannot be empty")
    private String location;

    private List<String> skillsRequired;

    private JobStatus status;  // Use Enum for job status
}