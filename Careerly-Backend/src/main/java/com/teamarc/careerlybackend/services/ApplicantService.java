package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.*;
import com.teamarc.careerlybackend.entity.*;
import com.teamarc.careerlybackend.entity.enums.ApplicationStatus;
import com.teamarc.careerlybackend.exceptions.ResourceNotFoundException;
import com.teamarc.careerlybackend.repository.ApplicantRepository;
import com.teamarc.careerlybackend.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final ModelMapper modelMapper;
    private final JobService jobService;

    @Transactional
    public JobApplicationDTO applyJob(JobApplicationDTO jobApplicationDTO) {
        if (jobApplicationDTO == null || jobApplicationDTO.getJobId() == null) {
            throw new IllegalArgumentException("Invalid job application data");
        }
        JobApplication jobApplication = modelMapper.map(jobApplicationDTO, JobApplication.class);
        jobApplication.setApplicationStatus(ApplicationStatus.APPLIED);
        jobApplication.setJob(jobService.getJobById(jobApplicationDTO.getJobId()));
        JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);
        return modelMapper.map(savedJobApplication, JobApplicationDTO.class);
    }

    @Transactional
    public JobApplicationDTO withdrawApplication(Long applicationId) {
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found"));
        jobApplication.setApplicationStatus(ApplicationStatus.WITHDRAWN);
        JobApplicationDTO jobApplicationDTO = modelMapper.map(jobApplication, JobApplicationDTO.class);
        jobApplicationRepository.delete(jobApplication);
        return jobApplicationDTO;
    }

//    @Transactional
//    public RatingDTO rateSession(Long sessionId, RatingDTO rating) {
//        Session session = sessionRepository.findById(sessionId)
//                .orElseThrow(() -> new RuntimeException("Session not found"));
//        session.setRating(modelMapper.map(rating, Rating.class));
//        Session savedSession = sessionRepository.save(session);
//        return modelMapper.map(savedSession, RatingDTO.class);
//    }

    public ApplicantDTO getMyProfile() {
        Applicant applicant = getCurrentApplicant();
        return modelMapper.map(applicant, ApplicantDTO.class);

    }

    public Page<JobApplicationDTO> getAllApplications(PageRequest pageRequest) {
        Page<JobApplication> jobApplications = jobApplicationRepository.findAll(pageRequest);
        return jobApplications.map(job -> modelMapper.map(job, JobApplicationDTO.class));
    }

    public Applicant getCurrentApplicant() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return applicantRepository.findByUser(user)
                .orElseThrow(()-> new ResourceNotFoundException("Applicant not associated with user with id: "+ user.getId()));

    }
    public Applicant createNewApplicant(User user) {
        Applicant applicant= Applicant.builder()
                .user(user)
                .jobApplications(null)
                .resume(null)
                .build();
        return applicantRepository.save(applicant);
    }
}