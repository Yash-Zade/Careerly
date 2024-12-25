package com.teamarc.careerlybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO extends UserDTO {

    private List<PaymentDTO> payments;
}
