package com.teamarc.careerlybackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantDTO extends UserDTO {

    private String resume;

    @NotNull(message = "Wallet cannot be null")
    private WalletDTO wallet;

    private List<JobDTO> appliedJobs;
}
