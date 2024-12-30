package com.teamarc.careerlybackend.controller;

import com.teamarc.careerlybackend.dto.*;
import com.teamarc.careerlybackend.services.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/applicants")
@Secured("ROLE_APPLICANT")
public class ApplicantController {

    private final ApplicantService applicantService;


    @GetMapping(path="/profile")
    public ResponseEntity<ApplicantDTO> getMyProfile(){
        return ResponseEntity.ok(applicantService.getMyProfile());
    }

    @PutMapping(path="/profile/{id}")
    public ResponseEntity<ApplicantDTO> updateProfile(@RequestBody Map<String, Object> object, @PathVariable Long id){
        return ResponseEntity.ok(applicantService.updateProfile(id, object));
    }


    @GetMapping(path="/jobs")
    public ResponseEntity<Page<JobDTO>> getJobs(@RequestParam(defaultValue = "0") Integer pageOffset,
                                               @RequestParam(defaultValue = "10", required = false) Integer pageSize){
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "postedDate", "jobId"));
        return ResponseEntity.ok(applicantService.getJobs(pageRequest));
    }

    @PostMapping(path="/jobs/{jobId}/apply")
    public ResponseEntity<JobApplicationDTO> applyForJob(@PathVariable Long jobId, @RequestBody JobApplicationDTO jobApplication){
        return ResponseEntity.ok(applicantService.applyJob(jobId,jobApplication));
    }

    @PostMapping(path="/jobs/{applicationId}/withdraw")
    public ResponseEntity<JobApplicationDTO> withdrawJobApplication(@PathVariable Long applicationId){
        return ResponseEntity.ok(applicantService.withdrawApplication(applicationId));
    }


    @GetMapping(path="/jobs/search")
    public ResponseEntity<Page<JobDTO>> searchJob(@RequestParam String keyword,
                                                  @RequestParam(defaultValue = "0") Integer pageOffset,
                                                  @RequestParam(defaultValue = "10", required = false) Integer pageSize, Pageable pageable){
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "postedDate", "jobId"));
        return ResponseEntity.ok(applicantService.searchJob(keyword, pageRequest,pageable));
    }


    @GetMapping(path="/job-applications")
    public ResponseEntity<Page<JobApplicationDTO>> getJobApplications(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                                     @RequestParam(defaultValue = "10", required = false) Integer pageSize, Pageable pageable){
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "appliedDate", "applicationId"));
        return ResponseEntity.ok(applicantService.getJobApplications(pageRequest,pageable));
    }


    @GetMapping(path="/applications/search")
    public ResponseEntity<Page<JobApplicationDTO>> searchApplications(@RequestParam String keyword,
                                                                      @RequestParam(defaultValue = "0") Integer pageOffset,
                                                                      @RequestParam(defaultValue = "10", required = false) Integer pageSize, Pageable pageable){
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "appliedDate", "applicationId"));
        return ResponseEntity.ok(applicantService.searchApplications(keyword, pageRequest,pageable));
    }

    @GetMapping(path="/applications/{applicationId}/status")
    public ResponseEntity<String> checkApplicationStatus(@PathVariable Long applicationId){
        String status = applicantService.checkApplicationStatus(applicationId);
        return ResponseEntity.ok(status);
    }


    @PostMapping(path="/resume/upload")
    public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file){
        applicantService.uploadResume(file);
        return ResponseEntity.ok("Resume uploaded successfully");
    }
}