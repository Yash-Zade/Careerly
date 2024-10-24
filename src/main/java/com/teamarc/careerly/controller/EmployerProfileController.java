package com.teamarc.careerly.controller;

import com.teamarc.careerly.dto.EmployerProfileDto;
import com.teamarc.careerly.entities.EmployerProfile;
import com.teamarc.careerly.services.EmployerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/employers")
public class EmployerProfileController {

    private final EmployerProfileService employerProfileService;

    @PostMapping("/create")
    public ResponseEntity<EmployerProfile> createEmployerProfile(@Valid @RequestBody EmployerProfileDto employerProfileDTO) {
        EmployerProfile createdProfile = employerProfileService.createEmployerProfile(employerProfileDTO);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EmployerProfile> updateEmployerProfile(@Valid @PathVariable Long id, @RequestBody EmployerProfileDto employerProfileDTO) {
        EmployerProfile updatedProfile = employerProfileService.updateEmployerProfile(id, employerProfileDTO);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployerProfile> getEmployerProfile(@Valid @PathVariable Long id) {
        EmployerProfile employerProfile = employerProfileService.getEmployerProfile(id);
        return new ResponseEntity<>(employerProfile, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProfile(@Valid @PathVariable Long id) {
        employerProfileService.deleteEmployerProfile(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
