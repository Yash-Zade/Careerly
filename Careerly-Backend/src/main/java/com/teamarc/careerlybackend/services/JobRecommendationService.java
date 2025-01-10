package com.teamarc.careerlybackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
