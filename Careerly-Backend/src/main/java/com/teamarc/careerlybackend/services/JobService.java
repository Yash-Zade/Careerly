package com.teamarc.careerlybackend.services;


import com.teamarc.careerlybackend.dto.JobDTO;
import com.teamarc.careerlybackend.entity.Job;
import com.teamarc.careerlybackend.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final ModelMapper modelMapper;


    public JobDTO getJobById(Long jobId) {
        return modelMapper.map(jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found")), JobDTO.class);
    }

    public Page<JobDTO> searchJobs(String keyword, PageRequest pageRequest, Pageable pageable) {
        return jobRepository.searchJobs(keyword, pageRequest, pageable).map((element) -> modelMapper.map(element, JobDTO.class));
    }

    public Page<JobDTO> getAllJobs(PageRequest pageRequest) {
        return jobRepository.findAll(pageRequest).map((element) -> modelMapper.map(element, JobDTO.class));
    }
}
