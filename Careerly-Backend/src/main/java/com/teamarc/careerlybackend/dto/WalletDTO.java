package com.teamarc.careerlybackend.dto;

import com.teamarc.careerlybackend.entity.Payment;
import lombok.Data;

import java.util.List;

@Data
public class WalletDTO {
    private long id;
    private UserDTO user;
    private double balance;
    private List<Payment> payments;
}
