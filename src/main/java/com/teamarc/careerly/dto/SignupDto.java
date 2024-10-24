package com.teamarc.careerly.dto;

import com.teamarc.careerly.entities.enums.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid.")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    private String password;

    @NotNull(message = "Roles cannot be null.")
    private Set<Roles> roles;
}
