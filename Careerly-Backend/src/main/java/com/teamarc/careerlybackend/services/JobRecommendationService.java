package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.JobDTO;
import com.teamarc.careerlybackend.entity.Applicant;
import com.teamarc.careerlybackend.entity.Job;
import com.teamarc.careerlybackend.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobRecommendationService {

    private final JobRepository jobRepository;
    private final ModelMapper modelMapper;

    private static final double SKILL_MATCH_WEIGHT = 0.6;
    private static final double LOCATION_WEIGHT = 0.15;
    private static final double THRESHOLD = 0.75;

    public Page<JobDTO> recommendJobs(Applicant applicant, PageRequest pageRequest) {

        List<Job> allJobs = jobRepository.findAll();

        List<JobDTO> recommendedJobs=allJobs.stream()
                .map(job -> {
                    double score = calculateFinalScore(applicant, job);
                    return score >= THRESHOLD ? modelMapper.map(job, JobDTO.class) : null;
                })
                .filter(jobDTO -> jobDTO != null)
                .collect(Collectors.toList());
        return new PageImpl<>(recommendedJobs, pageRequest, recommendedJobs.size());
    }

    private double calculateFinalScore(Applicant applicant, Job job) {
        double skillMatch = calculateSkillMatch(applicant.getSkills(), job.getSkillsRequired());
        double locationMatch = calculateLocationMatch(applicant.getPreferredLocations(), job.getLocation());

        return skillMatch * SKILL_MATCH_WEIGHT
                + locationMatch * LOCATION_WEIGHT;
    }

    private double calculateSkillMatch(List<String> applicantSkills, List<String> jobSkills) {
        if (jobSkills == null || jobSkills.isEmpty()) return 0;
        long matchCount = jobSkills.stream().filter(applicantSkills::contains).count();
        return (double) matchCount / jobSkills.size();
    }

    private double calculateLocationMatch(List<String> preferredLocations, String jobLocation) {
        if (preferredLocations == null || preferredLocations.isEmpty()) return 0;
        return preferredLocations.contains(jobLocation) ? 1.0 : 0.0;
    }
}
