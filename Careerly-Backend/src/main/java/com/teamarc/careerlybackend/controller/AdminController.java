package com.teamarc.careerlybackend.controller;

import com.teamarc.careerlybackend.dto.EmployerDTO;
import com.teamarc.careerlybackend.dto.MentorDTO;
import com.teamarc.careerlybackend.dto.OnBoardNewEmployerDTO;
import com.teamarc.careerlybackend.dto.OnboardNewMentorDTO;
import com.teamarc.careerlybackend.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/admin")
@RequiredArgsConstructor
@Secured("ROLE_ADMIN")
public class AdminController {

    private final AdminService adminService;


    @PostMapping(path="onBoardNewEmployer/{userId}")
    public ResponseEntity<EmployerDTO> onBoardNewEmployer(@PathVariable Long userId, @RequestBody OnBoardNewEmployerDTO onBoardNewEmployerDTO){
        return new ResponseEntity<>(adminService.onboardNewEmployer(userId,onBoardNewEmployerDTO), HttpStatus.CREATED);
    }


    @PostMapping(path="onBoardNewMentor/{userId}")
    public ResponseEntity<MentorDTO> onBoardNewMentor(@PathVariable Long userId, @RequestBody OnboardNewMentorDTO onboardNewMentorDTO){
        return new ResponseEntity<>(adminService.onboardNewMentor(userId, onboardNewMentorDTO), HttpStatus.CREATED);
    }
}
