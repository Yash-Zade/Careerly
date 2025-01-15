package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.*;
import com.teamarc.careerlybackend.entity.*;
import com.teamarc.careerlybackend.entity.enums.ApplicationStatus;
import com.teamarc.careerlybackend.entity.enums.SessionStatus;
import com.teamarc.careerlybackend.exceptions.ResourceNotFoundException;
import com.teamarc.careerlybackend.repository.ApplicantRepository;
import com.teamarc.careerlybackend.repository.JobApplicationRepository;
import com.teamarc.careerlybackend.repository.OnboardNewEmployerRepository;
import com.teamarc.careerlybackend.repository.OnboardNewMentorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.AmqpTemplate;
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
    private final AmqpTemplate amqpTemplate;
    private final OnboardNewEmployerRepository onboardNewEmployerRepository;
    private final OnboardNewMentorRepository onboardNewMentorRepository;
    private final WalletService walletService;
    private final RabbitMQService rabbitMQService;


    @Transactional
    public JobApplicationDTO applyJob(Long jobId, JobApplicationDTO jobApplicationDTO) {

        Job job = modelMapper.map(jobService.getJobById(jobId), Job.class);
        if (job.getJobStatus().toString().equals("CLOSED")) {
            throw new RuntimeException("Job is closed");
        }

        if (jobApplicationDTO == null || jobApplicationDTO.getJobId() == null) {
            throw new IllegalArgumentException("Invalid job application data");
        }

        JobApplication jobApplication = modelMapper.map(jobApplicationDTO, JobApplication.class);
        jobApplication.setApplicationStatus(ApplicationStatus.APPLIED);
        jobApplication.setJob(modelMapper.map(jobService.getJobById(jobApplicationDTO.getJobId()), Job.class));
        JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);
        EmailRequest emailRequest = EmailRequest.builder()
                .toEmail(savedJobApplication.getApplicant().getUser().getEmail())
                .subject("New Job Application")
                .body("A new job application has been submitted for job: " + savedJobApplication.getJob().getTitle())
                .buttonText("View Application")
                .buttonUrl("http://localhost:8080/job-applications/" + savedJobApplication.getApplicationId())
                .build();

        amqpTemplate.convertAndSend("emailQueue", emailRequest);

        return modelMapper.map(savedJobApplication, JobApplicationDTO.class);
    }

    @Transactional
    public JobApplicationDTO withdrawApplication(Long applicationId) {
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found"));
        jobApplication.setApplicationStatus(ApplicationStatus.WITHDRAWN);
        JobApplicationDTO jobApplicationDTO = modelMapper.map(jobApplication, JobApplicationDTO.class);
        jobApplicationRepository.delete(jobApplication);

        EmailRequest emailRequest = EmailRequest.builder()
                .toEmail(jobApplication.getApplicant().getUser().getEmail())
                .subject("Job Application Withdrawn")
                .body("Your job application has been withdrawn for job: " + jobApplication.getJob().getTitle())
                .buttonText("View Application")
                .buttonUrl("http://localhost:8080/job-applications/" + jobApplication.getApplicationId())
                .build();

        rabbitMQService.sendEmail(emailRequest);
        return jobApplicationDTO;
    }

    public ApplicantDTO getApplicantProfile() {
        Applicant applicant = getCurrentApplicant();
        return modelMapper.map(applicant, ApplicantDTO.class);

    }

    public Page<JobApplicationDTO> getAllJobApplications(PageRequest pageRequest, Pageable pageable) {
        Page<JobApplication> jobApplications = jobApplicationRepository.findByApplicant(getCurrentApplicant(), pageRequest, pageable);
        return jobApplications.map(jobApplication -> modelMapper.map(jobApplication, JobApplicationDTO.class));
    }

    public Applicant getCurrentApplicant() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return applicantRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Applicant not associated with user with id: " + user.getId()));

    }

    public Applicant createNewApplicant(User user) {
        Applicant applicant = Applicant.builder()
                .user(user)
                .jobApplications(null)
                .resume(null)
                .build();
        return applicantRepository.save(applicant);
    }

    public ApplicantDTO updateProfile(Long applicantId, Map<String, Object> updates) {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new ResourceNotFoundException("Applicant not found with id: " + applicantId));
        updates.forEach((field, value) -> {
            Field fieldToBeUpdated = ReflectionUtils.findRequiredField(Applicant.class, field);
            fieldToBeUpdated.setAccessible(true);
            ReflectionUtils.setField(fieldToBeUpdated, applicant, value);
        });
        return modelMapper.map(applicantRepository.save(applicant), ApplicantDTO.class);
    }

    public Page<JobApplicationDTO> searchApplications(String keyword, PageRequest pageRequest, Pageable pageable) {
        Page<JobApplication> jobApplications = jobApplicationRepository.searchApplications(keyword, pageRequest, pageable);
        return jobApplications.map(jobApplication -> modelMapper.map(jobApplication, JobApplicationDTO.class));
    }


    public void uploadResume(MultipartFile file) {
        Applicant applicant = getCurrentApplicant();
        applicant.setResume(file.getOriginalFilename());
        applicantRepository.save(applicant);
    }

    public String checkApplicationStatus(Long applicationId) {
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId).orElseThrow(() -> new ResourceNotFoundException("Job application not found"));
        return jobApplication.getApplicationStatus().name();
    }

    public ApplicantDTO getApplicantById(Long applicantId) {
        return modelMapper.map(applicantRepository.findById(applicantId)
                .orElseThrow(() -> new ResourceNotFoundException("Applicant not found with id: " + applicantId)), ApplicantDTO.class);
    }


    public SessionDTO requestSession(Long sessionId) {
        ApplicantDTO applicant = getApplicantProfile();
        return sessionManagementService.requestSession(sessionId, applicant);
    }


    public MentorProfileDTO rateMentor(RatingDTO ratingDTO, Long sessionId) {
        Session session = modelMapper.map(sessionService.getSessionById(sessionId), Session.class);
        Applicant applicant = getCurrentApplicant();
        if (!applicant.equals(session.getApplicant())) {
            throw new RuntimeException("Applicant is not the owner of session");
        }
        if (!session.getSessionStatus().equals(SessionStatus.COMPLETED)) {
            throw new RuntimeException("Session status is not ended hence cannot be Rated, status: " + session.getSessionStatus());
        }

        return ratingService.rateMentor(ratingDTO);
    }

    public SessionDTO joinSession(Long sessionId, String otp) {
        return sessionManagementService.joinSession(sessionId, otp);
    }

    public SessionDTO endSession(Long sessionId) {
        return sessionManagementService.endSessionByApplicant(sessionId);
    }

    public JobApplication getApplicationById(Long applicationId) {
        return jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found with id: " + applicationId));
    }

    public boolean isOwnerOfApplication(Long applicationId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobApplication jobApplication = getApplicationById(applicationId);
        ApplicantDTO applicant = getApplicantById(jobApplication.getApplicationId());
        User applicationUser = modelMapper.map(applicant.getUser(), User.class);
        return user.equals(applicationUser);
    }

    public boolean isOwnerOfSession(Long sessionId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SessionDTO session = sessionService.getSessionById(sessionId);
        ApplicantDTO applicant = getApplicantById(session.getApplicantId());
        User sessionUser = modelMapper.map(applicant.getUser(), User.class);
        return user.equals(sessionUser);
    }

    public boolean isOwnerOfProfile(Long applicantId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicantDTO applicant = getApplicantById(applicantId);
        User applicantUser = modelMapper.map(applicant.getUser(), User.class);
        return user.equals(applicantUser);
    }

    public SessionDTO cancelSession(Long sessionId) {
        return sessionManagementService.cancelSession(sessionId);
    }

    public void requestEmployerOnboard(OnBoardNewEmployerDTO onboardNewEmployerDTO) {
        EmailRequest emailRequest = EmailRequest.builder()
                .toEmail(getCurrentApplicant().getUser().getEmail())
                .subject("Employer Onboarding Request")
                .body("Your request for employer onboarding has been submitted")
                .buttonText("View")
                .buttonUrl("https://your-site.com")
                .build();
        rabbitMQService.sendEmail(emailRequest);
        onboardNewEmployerRepository.save(modelMapper.map(onboardNewEmployerDTO, OnboardNewEmployer.class));
    }

    public void requestMentorOnboard(OnboardNewMentorDTO onboardNewMentorDTO) {
        EmailRequest emailRequest = EmailRequest.builder()
                .toEmail(getCurrentApplicant().getUser().getEmail())
                .subject("Mentor Onboarding Request")
                .body("Your request for mentor onboarding has been submitted")
                .buttonText("View")
                .buttonUrl("https://your-site.com")
                .build();
        rabbitMQService.sendEmail(emailRequest);
        onboardNewMentorRepository.save(modelMapper.map(onboardNewMentorDTO, OnboardNewMentor.class));
    }

    public WalletDTO getWallet() {
        Wallet wallet = walletService.getWalletByUserId(getCurrentApplicant().getUser().getId());
        return modelMapper.map(wallet, WalletDTO.class);
    }
}