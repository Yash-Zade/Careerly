package com.teamarc.careerly.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;


@Data
public class EmployeeProfileDto {
    @NotBlank(message = "First name is mandatory")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @Size(max = 250, message = "Bio must not exceed 250 characters")
    private String bio;

    private Set<@Size(max = 30, message = "Skill must not exceed 30 characters") String> skills;

    private Set<@Size(max = 100, message = "Project name must not exceed 100 characters") String> projects;

    private Set<@Size(max = 100, message = "Education entry must not exceed 100 characters") String> education;

}
