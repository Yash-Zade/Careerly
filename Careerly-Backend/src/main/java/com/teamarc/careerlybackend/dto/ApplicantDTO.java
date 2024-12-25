package com.teamarc.careerlybackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantDTO extends UserDTO {

    private String resume;

    @NotNull(message = "Wallet cannot be null")
    private WalletDTO wallet;

    private List<JobDTO> appliedJobs;
}
