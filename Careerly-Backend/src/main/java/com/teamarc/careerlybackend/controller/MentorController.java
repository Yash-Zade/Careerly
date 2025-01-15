package com.teamarc.careerlybackend.controller;

import com.teamarc.careerlybackend.dto.MentorProfileDTO;
import com.teamarc.careerlybackend.dto.RatingDTO;
import com.teamarc.careerlybackend.dto.SessionDTO;
import com.teamarc.careerlybackend.services.MentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/mentors")
@RequiredArgsConstructor
@Secured("ROLE_MENTOR")
public class MentorController {

    private final MentorService mentorService;

    @GetMapping(path = "/profile")
    public ResponseEntity<MentorProfileDTO> getMentorProfile() {
        return ResponseEntity.ok(mentorService.getMentorProfile());
    }

    @PreAuthorize("@mentorService.isOwnerOfProfile(#id)")
    @PutMapping(path = "/profile/{id}")
    public ResponseEntity<MentorProfileDTO> updateMentorProfile(@RequestBody Map<String, Object> object, @PathVariable Long id) {
        return ResponseEntity.ok(mentorService.updateProfile(id, object));
    }

    @GetMapping(path = "/profile/rating")
    public ResponseEntity<Double> getMentorsAverageRating() {
        return ResponseEntity.ok(mentorService.getMentorsAverageRating());
    }


    @PreAuthorize("@mentorService.isOwnerOfSession(#id)")
    @GetMapping(path = "/sessions/{id}")
    public ResponseEntity<SessionDTO> getSessionById(@PathVariable Long id) {
        return ResponseEntity.ok(mentorService.getSessionById(id));
    }

    @PostMapping(path = "/sessions")
    public ResponseEntity<SessionDTO> createSession(@RequestBody SessionDTO session) {
        return ResponseEntity.ok(mentorService.createSession(session));
    }

    @PreAuthorize("@mentorService.isOwnerOfSession(#id)")
    @PutMapping(path = "/sessions/{id}")
    public ResponseEntity<SessionDTO> updateSession(@RequestBody Map<String, Object> object, @PathVariable Long id) {
        return ResponseEntity.ok(mentorService.updateSession(id, object));
    }

    @PreAuthorize("@mentorService.isOwnerOfSession(#id)")
    @PostMapping(path = "/sessions/{id}/accept")
    public ResponseEntity<SessionDTO> acceptSession(@PathVariable Long id) {
        return ResponseEntity.ok(mentorService.acceptSession(id));
    }

    @PreAuthorize("@mentorService.isOwnerOfSession(#sessionId)")
    @PostMapping(path = "/sessions/{sessionId}/cancelled")
    public ResponseEntity<SessionDTO> cancelSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(mentorService.cancelSession(sessionId));
    }

    @PreAuthorize("@mentorService.isOwnerOfSession(#sessionId)")
    @PostMapping(path = "/sessions/{sessionId}/end")
    public ResponseEntity<SessionDTO> endSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(mentorService.endSession(sessionId));
    }

    @PreAuthorize("@mentorService.isOwnerOfSession(#sessionId)")
    @PostMapping(path = "/sessions/{sessionId}/start")
    public ResponseEntity<SessionDTO> startSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(mentorService.startSession(sessionId));
    }

    @PreAuthorize("@mentorService.isOwnerOfSession(#sessionId)")
    @GetMapping(path = "/sessions/{sessionId}/rating")
    public ResponseEntity<RatingDTO> rateMentor(@PathVariable Long sessionId) {
        return ResponseEntity.ok(mentorService.rateMentor(sessionId));
    }

    @GetMapping(path = "/sessions/ratings")
    public ResponseEntity<Page<RatingDTO>> getRatings(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                      @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        return ResponseEntity.ok(mentorService.getRatings(pageOffset, pageSize));
    }


}
