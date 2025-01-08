package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.ApplicantDTO;
import com.teamarc.careerlybackend.dto.EmployerDTO;
import com.teamarc.careerlybackend.dto.JobApplicationDTO;
import com.teamarc.careerlybackend.dto.JobDTO;
import com.teamarc.careerlybackend.entity.*;
import com.teamarc.careerlybackend.entity.enums.ApplicationStatus;
import com.teamarc.careerlybackend.entity.enums.JobStatus;
import com.teamarc.careerlybackend.exceptions.ResourceNotFoundException;
import com.teamarc.careerlybackend.repository.EmployerRepository;
import com.teamarc.careerlybackend.repository.JobApplicationRepository;
import com.teamarc.careerlybackend.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployerService {
    private final EmployerRepository employerRepository;
    private final ModelMapper modelMapper;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final ApplicantService applicantService;

    public Employer createNewEmployer(Employer employer) {
        return employerRepository.save(employer);
    }

    public EmployerDTO getEmployerProfileById() {
        Employer employer = getCurrentEmployer();
        return modelMapper.map(employer, EmployerDTO.class);
    }

    private Employer getCurrentEmployer() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return employerRepository.findByUser(user)
                .orElseThrow(()-> new ResourceNotFoundException("Applicant not associated with user with id: "+ user.getId()));

    }

    public Page<JobApplicationDTO> getAllApplications(Long jobId, PageRequest pageRequest, Pageable pageable) {
        return jobApplicationRepository.findByJob_JobId(jobId, pageRequest, pageable)
                .map(jobApplication -> modelMapper.map(jobApplication, JobApplicationDTO.class));
    }

    public Page<JobDTO> getAllJobs(PageRequest pageRequest) {
        return jobRepository.findAll(pageRequest)
                .map(job-> modelMapper.map(job, JobDTO.class));
    }

    public JobDTO createJob(JobDTO job) {
        Job newJob = modelMapper.map(job, Job.class);
        newJob.setJobStatus(JobStatus.OPEN);
        Job savedJob = jobRepository.save(newJob);
        return modelMapper.map(savedJob, JobDTO.class);
    }

    public JobDTO updateJob(Long jobId, Map<String, Object> updates) {
        checkApplicantExistsById(jobId);
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        updates.forEach((field, value) -> {
            Field fieldToBeUpdated = ReflectionUtils.findRequiredField(Applicant.class, field);
            fieldToBeUpdated.setAccessible(true);
            ReflectionUtils.setField(fieldToBeUpdated, job, value);
        });
        return modelMapper.map(jobRepository.save(job), JobDTO.class);
    }


    public void checkApplicantExistsById(Long jobId) {
        if (!jobRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("Job not found with id: " + jobId);
        }
    }

    public JobDTO deleteJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        jobRepository.delete(job);
        return modelMapper.map(job, JobDTO.class);
    }

    public JobDTO getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        return modelMapper.map(job, JobDTO.class);
    }

    public Page<JobDTO> searchJobs(String keyword, PageRequest pageRequest, Pageable pageable) {
        Page<Job> jobs = jobRepository.searchJobs(keyword, pageRequest, pageable);
        return jobs.map(job -> modelMapper.map(job, JobDTO.class));
    }

    public Page<JobDTO> searchApplications(String keyword, PageRequest pageRequest, Pageable pageable) {
        Page<Job> jobs = jobRepository.searchJobs(keyword, pageRequest, pageable);
        return jobs.map(job -> modelMapper.map(job, JobDTO.class));
    }

    public ApplicationStatus checkApplicationStatus(Long applicationId) {
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Application not found with id: " + applicationId));
        return jobApplication.getApplicationStatus();
    }

    public Page<ApplicantDTO> searchApplicants(String keyword, PageRequest pageRequest, Pageable pageable) {
        Page<Applicant> applicants = jobApplicationRepository.searchApplicants(keyword, pageRequest, pageable);
        return applicants.map(applicant -> modelMapper.map(applicant, ApplicantDTO.class));
    }

    public ApplicantDTO getApplicantById(Long applicantId) {
        return applicantService.getApplicantById(applicantId);
    }


    public Page<ApplicantDTO> getApplicantsByJobId(Long jobId, PageRequest pageRequest, Pageable pageable) {
        return jobApplicationRepository.findByJob_JobId(jobId, pageRequest, pageable)
                .map(jobApplication -> modelMapper.map(jobApplication.getApplicant(), ApplicantDTO.class));
    }

    public JobApplicationDTO getJobApplicationByApplicantId(Long jobId, Long applicantId) {
        JobApplication jobApplication = jobApplicationRepository.findByJob_JobIdAndApplicant_ApplicantId(jobId, applicantId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Application not found"));
        return modelMapper.map(jobApplication, JobApplicationDTO.class);
    }

    public JobApplicationDTO withdrawApplicant(Long applicationId) {
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Application not found"));
        jobApplication.setApplicationStatus(ApplicationStatus.WITHDRAWN);
        return modelMapper.map(jobApplicationRepository.save(jobApplication), JobApplicationDTO.class);
    }

    public JobApplicationDTO changeApplicationStatus(Long applicationId, String status) {
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Application not found"));
        jobApplication.setApplicationStatus(ApplicationStatus.valueOf(status));
        return modelMapper.map(jobApplicationRepository.save(jobApplication), JobApplicationDTO.class);
    }

    public JobApplicationDTO getAllApplicantOfJobApplication(Long applicationId) {
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Application not found"));
        return modelMapper.map(jobApplication, JobApplicationDTO.class);
    }

    public EmployerDTO updateEmployerProfile(Long id, Map<String, Object> object) {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found with id: " + id));
        object.forEach((field, value) -> {
            Field fieldToBeUpdated = ReflectionUtils.findRequiredField(Employer.class, field);
            fieldToBeUpdated.setAccessible(true);
            ReflectionUtils.setField(fieldToBeUpdated, employer, value);
        });
        return modelMapper.map(employerRepository.save(employer), EmployerDTO.class);
    }

    public Page<JobApplicationDTO> getApplicationsByStatus(Long jobId, String status, PageRequest pageRequest, Pageable pageable) {
        return jobApplicationRepository.findByApplicationStatus(jobId, ApplicationStatus.valueOf(status), pageRequest, pageable)
                .map(jobApplication -> modelMapper.map(jobApplication, JobApplicationDTO.class));
    }

    public JobDTO closeJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        job.setJobStatus(JobStatus.CLOSED);
        return modelMapper.map(jobRepository.save(job), JobDTO.class);
    }

    public EmployerDTO getEmployerProfileById(Long id) {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found with id: " + id));
        return modelMapper.map(employer, EmployerDTO.class);
    }

    public boolean isOwnerOfJob(Long jobId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Job job = modelMapper.map(getJobById(jobId), Job.class);
        EmployerDTO employer = getEmployerProfileById(job.getPostedBy().getEmployerId());
        User jobUser = modelMapper.map(employer.getUser(), User.class);
        return user.equals(jobUser);
    }

    public boolean isOwnerOfApplication(Long applicationId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobApplication jobApplication = getApplicationById(applicationId);
        EmployerDTO employerDTO = getEmployerProfileById(jobApplication.getJob().getPostedBy().getEmployerId());
        User applicationUser = modelMapper.map(employerDTO.getUser(), User.class);
        return user.equals(applicationUser);
    }

    public boolean isOwnerOfProfile(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EmployerDTO employerDTO = getEmployerProfileById(id);
        User profileUser = modelMapper.map(employerDTO.getUser(), User.class);
        return user.equals(profileUser);
    }

    private JobApplication getApplicationById(Long applicationId) {
        return jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Application not found with id: " + applicationId));
    }

}
