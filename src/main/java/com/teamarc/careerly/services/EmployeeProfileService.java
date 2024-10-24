package com.teamarc.careerly.services;


import com.teamarc.careerly.dto.EmployeeProfileDto;
import com.teamarc.careerly.entities.EmployeeProfile;
import com.teamarc.careerly.exceptions.ResourceNotFoundException;
import com.teamarc.careerly.repository.EmployeeProfileRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@AllArgsConstructor
@Service
public class EmployeeProfileService {

    private final EmployeeProfileRepository employeeProfileRepository;
    private final ModelMapper modelMapper;

    public EmployeeProfile createProfile(EmployeeProfileDto employeeProfileDto) {
        EmployeeProfile employeeProfile = modelMapper.map(employeeProfileDto, EmployeeProfile.class);
        return employeeProfileRepository.save(employeeProfile);
    }

    public EmployeeProfile updateProfile(Long id, EmployeeProfileDto employeeProfileDTO) {
        EmployeeProfile employeeProfile = employeeProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found with ID: " + id));

        for (Field field : employeeProfileDTO.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(employeeProfileDTO);
                if (value != null) {
                    Field entityField = EmployeeProfile.class.getDeclaredField(field.getName());
                    entityField.setAccessible(true);
                    entityField.set(employeeProfile, value);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.err.println("Error updating field: " + field.getName() + " - " + e.getMessage());
            }
        }

        return employeeProfileRepository.save(employeeProfile);
    }

    public EmployeeProfile getProfile(Long id) {
        return employeeProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found with ID: " + id));
    }

    public void deleteProfile(Long id) {
        EmployeeProfile employeeProfile = employeeProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found with ID: " + id));

        employeeProfileRepository.delete(employeeProfile);
    }
}

