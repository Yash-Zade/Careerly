package com.teamarc.careerly.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployerProfileDto {
    @NotBlank(message = "Company name is required.")
    private String companyName;

    @NotBlank(message = "Website is required.")
    private String website;

    @NotBlank(message = "Industry is required.")
    private String industry;

    @NotBlank(message = "Description is required.")
    @Size(max = 500, message = "Description cannot exceed 500 characters.")
    private String description;

    @NotBlank(message = "Address is required.")
    private String address;

    @NotBlank(message = "Contact number is required.")
    @Size(min = 10, max = 15, message = "Contact number must be between 10 and 15 characters.")
    private String contactNumber;
}
