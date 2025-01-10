package com.teamarc.careerlybackend.controller;

import com.teamarc.careerlybackend.dto.JobDTO;
import com.teamarc.careerlybackend.dto.MentorDTO;
import com.teamarc.careerlybackend.dto.SessionDTO;
import com.teamarc.careerlybackend.services.JobService;
import com.teamarc.careerlybackend.services.MentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public")
public class PublicControllers {

    private final JobService jobService;
    private final MentorService mentorService;


    @GetMapping("/jobs")
    public ResponseEntity<Page<JobDTO>> getAllJobs(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                   @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "postedDate", "jobId"));
        return ResponseEntity.ok(jobService.getAllJobs(pageRequest));
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @GetMapping("/mentors")
    public ResponseEntity<Page<MentorDTO>> getAllMentors(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                         @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ResponseEntity.ok(mentorService.getALLMentors(pageOffset, pageSize));
    }

    @GetMapping("/mentors/{id}")
    public ResponseEntity<MentorDTO> getProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(mentorService.getProfileById(id));
    }

    @GetMapping("/sessions")
    public ResponseEntity<Page<SessionDTO>> getAllSessions(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                           @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ResponseEntity.ok(mentorService.getSessions(pageOffset, pageSize));
    }

    @GetMapping("/sessions/{id}")
    public ResponseEntity<SessionDTO> getSessionById(@PathVariable Long id) {
        return ResponseEntity.ok(mentorService.getSessionById(id));
    }

}
