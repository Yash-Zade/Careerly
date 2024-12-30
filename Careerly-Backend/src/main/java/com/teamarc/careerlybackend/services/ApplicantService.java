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
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.lang.reflect.Field;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final ModelMapper modelMapper;
    private final JobService jobService;

    @Transactional
    public JobApplicationDTO applyJob(Long jobId, JobApplicationDTO jobApplicationDTO) {

        Job job = jobService.getJobById(jobId);
        if(job.getJobStatus().toString().equals("CLOSED")){
            throw new RuntimeException("Job is closed");
        }

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

    public Page<JobApplicationDTO> getJobApplications(PageRequest pageRequest, Pageable pageable) {
        Page<JobApplication> jobApplications = jobApplicationRepository.findByApplicant(getCurrentApplicant(), pageRequest, pageable);
        return jobApplications.map(jobApplication -> modelMapper.map(jobApplication, JobApplicationDTO.class));
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

    public ApplicantDTO updateProfile(Long applicantId, Map<String, Object> updates) {
        checkApplicantExistsById(applicantId);
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new ResourceNotFoundException("Applicant not found with id: " + applicantId));
        updates.forEach((field, value) -> {
            Field fieldToBeUpdated = ReflectionUtils.findRequiredField(Applicant.class, field);
            fieldToBeUpdated.setAccessible(true);
            ReflectionUtils.setField(fieldToBeUpdated, applicant, value);
        });
        return modelMapper.map(applicantRepository.save(applicant), ApplicantDTO.class);
    }


    public void checkApplicantExistsById(Long applicantId) {
        if (!applicantRepository.existsById(applicantId)) {
            throw new ResourceNotFoundException("Applicant not found with id: " + applicantId);
        }
    }

    public Page<JobApplicationDTO> searchApplications(String keyword, PageRequest pageRequest, Pageable pageable) {
        Page<JobApplication> jobApplications = jobApplicationRepository.searchApplications(keyword, pageRequest, pageable);
        return jobApplications.map(jobApplication -> modelMapper.map(jobApplication, JobApplicationDTO.class));
    }

    public Page<JobDTO> searchJob(String keyword, PageRequest pageRequest, Pageable pageable) {
        Page<Job> jobs = jobService.searchJobs(keyword, pageRequest, pageable);
        return jobs.map(job -> modelMapper.map(job, JobDTO.class));
    }

    public void uploadResume(MultipartFile file) {
        Applicant applicant = getCurrentApplicant();
        applicant.setResume(file.getOriginalFilename());
        applicantRepository.save(applicant);
    }

    public String checkApplicationStatus(Long applicationId) {
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId).orElseThrow(()-> new ResourceNotFoundException("Job application not found"));
        return jobApplication.getApplicationStatus().name();
    }

    public Applicant getApplicantById(Long applicantId) {
        return applicantRepository.findById(applicantId)
                .orElseThrow(()-> new ResourceNotFoundException("Applicant not found with id: "+applicantId));
    }

    public Page<JobDTO> getJobs(PageRequest pageRequest) {
        Page<Job> jobs = jobService.getAllJobs(pageRequest);
        return jobs.map(job -> modelMapper.map(job, JobDTO.class));
    }
}