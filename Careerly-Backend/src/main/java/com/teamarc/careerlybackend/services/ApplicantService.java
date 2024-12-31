package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.*;
import com.teamarc.careerlybackend.entity.*;
import com.teamarc.careerlybackend.entity.enums.ApplicationStatus;
import com.teamarc.careerlybackend.entity.enums.SessionStatus;
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
    private final SessionService sessionService;
    private final RatingService ratingService;
    private final SessionManagementService sessionManagementService;

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

    public ApplicantDTO getApplicantById(Long applicantId) {
        return modelMapper.map(applicantRepository.findById(applicantId)
                .orElseThrow(() -> new ResourceNotFoundException("Applicant not found with id: " + applicantId)), ApplicantDTO.class);
    }

    public Page<JobDTO> getJobs(PageRequest pageRequest) {
        Page<Job> jobs = jobService.getAllJobs(pageRequest);
        return jobs.map(job -> modelMapper.map(job, JobDTO.class));
    }

    public SessionDTO requestSession(Long sessionId) {
        ApplicantDTO applicant = getMyProfile();
        return sessionManagementService.requestSession(sessionId,applicant);
    }


    public MentorDTO rateMentor(RatingDTO ratingDTO,Long sessionId) {
        Session session= modelMapper.map(sessionService.getSessionById(sessionId), Session.class);
        Applicant applicant=getCurrentApplicant();
        if(!applicant.equals(session.getApplicant())){
            throw new RuntimeException("Applicant is not the owner of session");
        }
        if(!session.getSessionStatus().equals(SessionStatus.COMPLETED)){
            throw new RuntimeException("Session status is not ended hence cannot be Rated, status: "+session.getSessionStatus());
        }

        return ratingService.rateMentor(ratingDTO);
    }

    public SessionDTO joinSession(Long sessionId, String otp) {
        return sessionManagementService.joinSession(sessionId, otp);
    }

    public SessionDTO endSession(Long sessionId) {
        return sessionManagementService.endSessionByApplicant(sessionId);
    }
}