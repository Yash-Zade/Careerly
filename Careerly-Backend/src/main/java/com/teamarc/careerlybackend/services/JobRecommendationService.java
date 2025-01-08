package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.JobDTO;
import com.teamarc.careerlybackend.entity.Applicant;
import com.teamarc.careerlybackend.entity.Job;
import com.teamarc.careerlybackend.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobRecommendationService {

//    private final JobRepository jobRepository;
//    private final ModelMapper modelMapper;
//
//    private static final double SKILL_MATCH_WEIGHT = 0.6;
//    private static final double LOCATION_WEIGHT = 0.15;
//    private static final double THRESHOLD = 0.75;

//    public Page<JobDTO> recommendJobs(Applicant applicant, PageRequest pageRequest) {
//        Page<Job> jobsPage = jobRepository.findRecommendedJobs(applicant, SKILL_MATCH_WEIGHT, LOCATION_WEIGHT, pageRequest);
//
//        List<JobDTO> recommendedJobs = jobsPage.getContent().stream()
//                .map(job -> modelMapper.map(job, JobDTO.class))
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(recommendedJobs, pageRequest, jobsPage.getTotalElements());
//    }
}
