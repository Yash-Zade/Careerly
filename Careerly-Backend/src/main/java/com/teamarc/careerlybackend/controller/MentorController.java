package com.teamarc.careerlybackend.controller;

import com.teamarc.careerlybackend.dto.MentorDTO;
import com.teamarc.careerlybackend.dto.RatingDTO;
import com.teamarc.careerlybackend.dto.SessionDTO;
import com.teamarc.careerlybackend.services.MentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/mentor")
@RequiredArgsConstructor
public class MentorController {

    private final MentorService mentorService;

    @GetMapping(path = "/profile")
    public ResponseEntity<MentorDTO> getMyProfile(){
        return ResponseEntity.ok(mentorService.getMyProfile());
    }

    @GetMapping(path ="/profile/{id}")
    public ResponseEntity<MentorDTO> getProfileById(Long id){
        return ResponseEntity.ok(mentorService.getProfileById(id));
    }

    @PutMapping(path ="/profile/{id}")
    public ResponseEntity<MentorDTO> updateMentorProfile(@RequestBody Map<String, Object> object, @PathVariable Long id){
        return ResponseEntity.ok(mentorService.updateProfile(id, object));
    }

    @GetMapping(path ="/profile/rating")
    public ResponseEntity<Double> getMyRating(){
        return ResponseEntity.ok(mentorService.getMyRating());
    }


    @GetMapping(path ="/sessions")
    public ResponseEntity<Page<SessionDTO>> getSessions(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                       @RequestParam(defaultValue = "10", required = false) Integer pageSize){
        return ResponseEntity.ok(mentorService.getSessions(pageOffset, pageSize));
    }


    @GetMapping(path ="/sessions/{id}")
    public ResponseEntity<SessionDTO> getSessionById(@PathVariable Long id){
        return ResponseEntity.ok(mentorService.getSessionById(id));
    }

    @PostMapping(path ="/sessions")
    public ResponseEntity<SessionDTO> createSession(@RequestBody SessionDTO session){
        return ResponseEntity.ok(mentorService.createSession(session));
    }

    @PutMapping(path ="/sessions/{id}")
    public ResponseEntity<SessionDTO> updateSession(@RequestBody Map<String, Object> object, @PathVariable Long id){
        return ResponseEntity.ok(mentorService.updateSession(id, object));
    }
    @PostMapping(path ="/sessions/{id}/accept")
    public ResponseEntity<SessionDTO> acceptSession(@PathVariable Long id){
        return ResponseEntity.ok(mentorService.acceptSession(id));
    }

    @PostMapping(path ="/sessions/{sessionId}/cancelled")
    public ResponseEntity<SessionDTO> cancelSession(@PathVariable Long sessionId){
        return ResponseEntity.ok(mentorService.cancelSession(sessionId));
    }

    @PostMapping(path ="/sessions/{sessionId}/end")
    public ResponseEntity<SessionDTO> endSession(@PathVariable Long sessionId){
        return ResponseEntity.ok(mentorService.endSession(sessionId));
    }

    @PostMapping(path ="/sessions/{sessionId}/start")
    public ResponseEntity<SessionDTO> startSession(@PathVariable Long sessionId){
        return ResponseEntity.ok(mentorService.startSession(sessionId));
    }

    @GetMapping(path ="/sessions/{sessionId}/rating")
    public ResponseEntity<RatingDTO> rateMentor(@PathVariable Long sessionId){
        return ResponseEntity.ok(mentorService.rateMentor(sessionId));
    }

    @GetMapping(path ="/sessions/ratings")
    public ResponseEntity<Page<RatingDTO>> getRatings(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                     @RequestParam(defaultValue = "10", required = false) Integer pageSize){
        return ResponseEntity.ok(mentorService.getRatings(pageOffset, pageSize));
    }


}
