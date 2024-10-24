package com.teamarc.careerly.dto;

import com.teamarc.careerly.entities.enums.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Name is mandatory.")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters.")
    private String name;

    @NotBlank(message = "Email is mandatory.")
    @Email(message = "Email should be valid.")
    private String email;

    @NotNull(message = "Roles cannot be null.")
    private Set<Roles> roles;

}
