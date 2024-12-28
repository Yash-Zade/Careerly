package com.teamarc.careerlybackend.services;


import com.teamarc.careerlybackend.entity.Job;
import com.teamarc.careerlybackend.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;


    public Job getJobById(Long jobId) {
        return jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
    }

}
