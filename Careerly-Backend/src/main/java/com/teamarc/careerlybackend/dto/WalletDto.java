package com.teamarc.careerlybackend.dto;

import com.teamarc.careerly.entities.WalletTransaction;
import lombok.Data;

import java.util.List;

@Data
public class WalletDto {
    private long id;
    private UserDto user;
    private double balance;
    private List<WalletTransaction> transactions;
}
