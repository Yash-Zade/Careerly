package com.teamarc.careerlybackend.controller;

import com.teamarc.careerlybackend.dto.*;
import com.teamarc.careerlybackend.services.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/applicant")
@Secured("ROLE_APPLICANT")
public class ApplicantController {

    private final ApplicantService applicantService;

    @PostMapping(path="/applyJob")
    public ResponseEntity<JobApplicationDTO> applyJob(@RequestBody JobApplicationDTO jobApplication){
        return ResponseEntity.ok(applicantService.applyJob(jobApplication));
    }

    @PostMapping(path="/withdrawApplication/{applicationId}")
    public ResponseEntity<JobApplicationDTO> withdrawApplication(@PathVariable Long applicationId){
        return ResponseEntity.ok(applicantService.withdrawApplication(applicationId));
    }

//    @PostMapping(path="/rateSession")
//    public ResponseEntity<RatingDTO> rateSession(@RequestBody RatingDTO ratingDTO){
//        return ResponseEntity.ok(applicantService.rateSession(ratingDTO.getSession().getSessionId(), ratingDTO));
//    }

    @GetMapping(path="/getMyProfile")
    public ResponseEntity<ApplicantDTO> getMyProfile(){
        return ResponseEntity.ok(applicantService.getMyProfile());
    }

    @GetMapping(path="/getAllApplications")
    public ResponseEntity<Page<JobApplicationDTO>> getAllApplications(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                                      @RequestParam(defaultValue = "10", required = false) Integer pageSize){
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC,"appliedDate","applicationId"));
        return ResponseEntity.ok(applicantService.getAllApplications(pageRequest));
    }
}