package com.teamarc.careerlybackend.controller;

import com.teamarc.careerlybackend.services.JobRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

//    private final MentorRecommendationService mentorRecommendationService;
//    private final JobRecommendationService jobRecommendationService;
//
//    @PostMapping("/mentors")
//    public ResponseEntity<Page<MentorProfileDTO>> recommendMentors(@RequestBody RecommendationRequestDTO recommendationRequest,@RequestParam(defaultValue = "0") Integer pageOffset,
//                                                            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
//        return ResponseEntity.ok(mentorRecommendationService.recommendMentors(recommendationRequest, PageRequest.of(pageOffset, pageSize)));
//    }

//    @PostMapping("/jobs")
//    public ResponseEntity<Page<JobDTO>> recommendJobs(@RequestBody Applicant applicant,
//                                                      @RequestParam(defaultValue = "0") Integer pageOffset,
//                                                      @RequestParam(defaultValue = "10", required = false) Integer pageSize){
//        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "postedDate", "jobId"));
//        return ResponseEntity.ok(jobRecommendationService.recommendJobs(applicant, pageRequest));
//    }


}