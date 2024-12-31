package com.teamarc.careerlybackend.controller;

import com.teamarc.careerlybackend.dto.JobDTO;
import com.teamarc.careerlybackend.dto.MentorDTO;
import com.teamarc.careerlybackend.dto.RecommendationRequestDTO;

import com.teamarc.careerlybackend.entity.Applicant;
import com.teamarc.careerlybackend.entity.Job;
import com.teamarc.careerlybackend.services.JobRecommendationService;
import com.teamarc.careerlybackend.services.MentorRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final MentorRecommendationService mentorRecommendationService;
    private final JobRecommendationService jobRecommendationService;

    @PostMapping("/mentors")
    public ResponseEntity<List<MentorDTO>> recommendMentors(@RequestBody RecommendationRequestDTO recommendationRequest) {
        return ResponseEntity.ok(mentorRecommendationService.recommendMentors(recommendationRequest));
    }

    @PostMapping("/jobs")
    public ResponseEntity<Page<JobDTO>> recommendJobs(@RequestBody Applicant applicant,
                                                      @RequestParam(defaultValue = "0") Integer pageOffset,
                                                      @RequestParam(defaultValue = "10", required = false) Integer pageSize){
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "postedDate", "jobId"));
        return ResponseEntity.ok(jobRecommendationService.recommendJobs(applicant, pageRequest));
    }


}