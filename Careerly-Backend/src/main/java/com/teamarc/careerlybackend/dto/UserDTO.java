package com.teamarc.careerlybackend.dto;

import com.teamarc.careerlybackend.entity.enums.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private Long userId;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Role cannot be null")
    private Set<Roles> roles;

}
