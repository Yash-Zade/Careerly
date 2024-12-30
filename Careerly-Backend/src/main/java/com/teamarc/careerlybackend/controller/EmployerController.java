package com.teamarc.careerlybackend.controller;

import com.teamarc.careerlybackend.dto.ApplicantDTO;
import com.teamarc.careerlybackend.dto.EmployerDTO;
import com.teamarc.careerlybackend.dto.JobApplicationDTO;
import com.teamarc.careerlybackend.dto.JobDTO;
import com.teamarc.careerlybackend.services.EmployerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/employers")
@RequiredArgsConstructor
@Secured("ROLE_EMPLOYER")
public class EmployerController {

    private final EmployerService employerService;

    @GetMapping("/profile")
    public ResponseEntity<EmployerDTO> getMyProfile() {
        return ResponseEntity.ok(employerService.getMyProfile());
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<EmployerDTO> getEmployerProfile(@PathVariable Long id) {
        return ResponseEntity.ok(employerService.getEmployerProfile(id));
    }

    @PutMapping(path="/profile/{id}")
    public ResponseEntity<EmployerDTO> updateEmployerProfile(@RequestBody Map<String, Object> object, @PathVariable Long id){
        return ResponseEntity.ok(employerService.updateEmployerProfile(id, object));
    }

    @GetMapping("/job-applications")
    public ResponseEntity<Page<JobApplicationDTO>> getAllApplications(@PathVariable Long jobId,@RequestParam(defaultValue = "0") Integer pageOffset,
                                                                      @RequestParam(defaultValue = "10", required = false) Integer pageSize, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "appliedDate", "applicationId"));
        return ResponseEntity.ok(employerService.getAllApplications(jobId,pageRequest, pageable));
    }


    @GetMapping("/jobs")
    public ResponseEntity<Page<JobDTO>> getAllJobs(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                   @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "appliedDate", "applicationId"));
        return ResponseEntity.ok(employerService.getAllJobs(pageRequest));
    }

    @GetMapping("/jobs/{jobId}")
    public ResponseEntity<JobDTO> getJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(employerService.getJob(jobId));
    }

    @PostMapping("/jobs")
    public ResponseEntity<JobDTO> createJob(@RequestBody JobDTO job) {
        return ResponseEntity.ok(employerService.createJob(job));
    }

    @PutMapping("/jobs/{jobId}")
    public ResponseEntity<JobDTO> updateJob(@PathVariable Long jobId, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(employerService.updateJob(jobId, updates));
    }

    @DeleteMapping("/jobs/{jobId}")
    public ResponseEntity<JobDTO> deleteJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(employerService.deleteJob(jobId));
    }

    @PostMapping("/jobs/{jobId}/close")
    public ResponseEntity<JobDTO> closeJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(employerService.closeJob(jobId));
    }

    @GetMapping("/jobs/search")
    public ResponseEntity<Page<JobDTO>> searchJobs(@RequestParam String keyword, @RequestParam(defaultValue = "0") Integer pageOffset,
                                                   @RequestParam(defaultValue = "10", required = false) Integer pageSize, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "appliedDate", "applicationId"));
        return ResponseEntity.ok(employerService.searchJobs(keyword, pageRequest, pageable));
    }

    @GetMapping("/applications/search")
    public ResponseEntity<Page<JobDTO>> searchApplications(@RequestParam String keyword, @RequestParam(defaultValue = "0") Integer pageOffset,
                                                           @RequestParam(defaultValue = "10", required = false) Integer pageSize, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "appliedDate", "applicationId"));
        return ResponseEntity.ok(employerService.searchApplications(keyword, pageRequest, pageable));
    }

    @GetMapping("/applications/{applicationId}/status")
    public ResponseEntity<String> checkApplicationStatus(@PathVariable Long applicationId) {
        String status = employerService.checkApplicationStatus(applicationId);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/applicants/search")
    public ResponseEntity<Page<ApplicantDTO>> searchApplicants(@RequestParam String keyword, @RequestParam(defaultValue = "0") Integer pageOffset,
                                                               @RequestParam(defaultValue = "10", required = false) Integer pageSize, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "appliedDate", "applicationId"));
        return ResponseEntity.ok(employerService.searchApplicants(keyword, pageRequest, pageable));
    }

    @GetMapping("/applicants/{applicantId}")
    public ResponseEntity<ApplicantDTO> getApplicant(@PathVariable Long applicantId) {
        return ResponseEntity.ok(employerService.getApplicant(applicantId));
    }

    @GetMapping("/jobs/{jobId}/applicants")
    public ResponseEntity<Page<ApplicantDTO>> getApplicants(@PathVariable Long jobId, @RequestParam(defaultValue = "0") Integer pageOffset,
                                                            @RequestParam(defaultValue = "10", required = false) Integer pageSize, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "appliedDate", "applicationId"));
        return ResponseEntity.ok(employerService.getApplicants(jobId, pageRequest, pageable));
    }

    @GetMapping("/jobs/{jobId}/applicants/{applicantId}")
    public ResponseEntity<JobApplicationDTO> getApplicantApplication(@PathVariable Long jobId, @PathVariable Long applicantId) {
        return ResponseEntity.ok(employerService.getApplicantApplication(jobId, applicantId));
    }

    @PostMapping("/applications/{applicationId}/status")
    public ResponseEntity<JobApplicationDTO> changeApplicationStatus(@PathVariable Long applicationId, @RequestParam String status) {
        return ResponseEntity.ok(employerService.changeApplicationStatus(applicationId, status));
    }

    @PostMapping("/applications/{applicationId}/withdraw")
    public ResponseEntity<JobApplicationDTO> withdrawApplicant(@PathVariable Long applicationId) {
        return ResponseEntity.ok(employerService.withdrawApplicant(applicationId));
    }

    @GetMapping("/jobs/{jobId}/applications/{status}")
    public ResponseEntity<Page<JobApplicationDTO>> getApplicationsByStatus(@PathVariable Long jobId, @PathVariable String status, @RequestParam(defaultValue = "0") Integer pageOffset,
                                                                           @RequestParam(defaultValue = "10", required = false) Integer pageSize, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "appliedDate", "applicationId"));
        return ResponseEntity.ok(employerService.getApplicationsByStatus(jobId, status, pageRequest, pageable));
    }


    @GetMapping("/applications/{applicationId}/applicant")
    public ResponseEntity<JobApplicationDTO> getApplicationsApplicant(@PathVariable Long applicationId) {
        return ResponseEntity.ok(employerService.getApplicationsApplicant(applicationId));
    }

}