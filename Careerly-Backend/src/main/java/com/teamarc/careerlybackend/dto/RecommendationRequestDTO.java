package com.teamarc.careerlybackend.dto;

import com.teamarc.careerlybackend.entity.Applicant;
import com.teamarc.careerlybackend.entity.Job;
import lombok.Data;

@Data
public class RecommendationRequestDTO {
        private Applicant applicant;
        private Job job;

}
