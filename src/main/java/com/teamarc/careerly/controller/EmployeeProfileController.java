package com.teamarc.careerly.controller;


import com.teamarc.careerly.dto.EmployeeProfileDto;
import com.teamarc.careerly.entities.EmployeeProfile;
import com.teamarc.careerly.services.EmployeeProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/employees")
public class EmployeeProfileController {


    private final EmployeeProfileService employeeProfileService;

    @PostMapping("/create")
    public ResponseEntity<EmployeeProfile> createProfile(@Valid @RequestBody EmployeeProfileDto employeeProfileDTO) {
        EmployeeProfile createdProfile = employeeProfileService.createProfile(employeeProfileDTO);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeProfile> updateProfile(@Valid @PathVariable Long id, @RequestBody EmployeeProfileDto employeeProfileDto) {
        EmployeeProfile updatedProfile = employeeProfileService.updateProfile(id, employeeProfileDto);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeProfile> getProfile(@Valid @PathVariable Long id) {
        EmployeeProfile employeeProfile = employeeProfileService.getProfile(id);
        return new ResponseEntity<>(employeeProfile, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProfile(@Valid @PathVariable Long id) {
        employeeProfileService.deleteProfile(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}



