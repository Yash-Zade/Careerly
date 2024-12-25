package com.teamarc.careerlybackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import javax.management.relation.Role;

@Data
public class UserDTO {

    private Long userId;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Role cannot be null")
    private Role role;

    private String profilePicture;

    private Boolean isActive;
}
