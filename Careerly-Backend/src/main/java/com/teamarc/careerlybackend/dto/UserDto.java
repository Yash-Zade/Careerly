package com.teamarc.careerlybackend.dto;

import com.teamarc.careerly.entities.Session;
import com.teamarc.careerly.entities.enums.Roles;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
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
    private String name;

    private String email;

    private Set<Roles> roles;

    private Set<Session> sessions;

}
