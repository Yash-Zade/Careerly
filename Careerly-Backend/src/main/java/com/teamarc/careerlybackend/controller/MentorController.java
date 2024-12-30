package com.teamarc.careerlybackend.controller;

import com.teamarc.careerlybackend.dto.MentorDTO;
import com.teamarc.careerlybackend.services.MentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
