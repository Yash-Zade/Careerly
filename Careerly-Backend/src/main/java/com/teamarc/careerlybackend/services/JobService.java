package com.teamarc.careerlybackend.services;


import com.teamarc.careerlybackend.entity.Job;
import com.teamarc.careerlybackend.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;


    public Job getJobById(Long jobId) {
        return jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public Page<Job> searchJobs(String keyword, PageRequest pageRequest, Pageable pageable) {
        return jobRepository.searchJobs(keyword, pageRequest, pageable);
    }
}
