package com.teamarc.careerly.services;


import com.teamarc.careerly.dto.EmployerProfileDto;
import com.teamarc.careerly.entities.EmployerProfile;
import com.teamarc.careerly.exceptions.ResourceNotFoundException;
import com.teamarc.careerly.repository.EmployerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@RequiredArgsConstructor
@Service
public class EmployerProfileService {


    private final EmployerProfileRepository employerProfileRepository;
    private final ModelMapper modelMapper;

    public EmployerProfile createEmployerProfile(EmployerProfileDto employerProfileDTO) {
        EmployerProfile employerProfile = new EmployerProfile();
        modelMapper.map(employerProfileDTO, employerProfile);
        return employerProfileRepository.save(employerProfile);
    }

    public EmployerProfile updateEmployerProfile(Long id, EmployerProfileDto employerProfileDTO) {
        EmployerProfile employerProfile = employerProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employer profile not found"));
        modelMapper.map(employerProfileDTO, employerProfile);
        Field[] dtoFields = employerProfileDTO.getClass().getDeclaredFields();
        for (Field dtoField : dtoFields) {
            dtoField.setAccessible(true);
            try {
                Object value = dtoField.get(employerProfileDTO);
                if (value != null) {
                    Field entityField = EmployerProfile.class.getDeclaredField(dtoField.getName());
                    entityField.setAccessible(true);
                    entityField.set(employerProfile, value);
                }
            } catch (NoSuchFieldException e) {
                System.out.println("No such field: " + dtoField.getName() + " in entity.");
            } catch (IllegalAccessException e) {
                System.out.println("Error accessing field: " + dtoField.getName() + " - " + e.getMessage());
            }
        }

        return employerProfileRepository.save(employerProfile);
    }

    public EmployerProfile getEmployerProfile(Long id) {
        return employerProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employer profile not found"));
    }

    public void deleteEmployerProfile(Long id) {
        EmployerProfile employeeProfile = employerProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found"));

        employerProfileRepository.delete(employeeProfile);
    }


}
