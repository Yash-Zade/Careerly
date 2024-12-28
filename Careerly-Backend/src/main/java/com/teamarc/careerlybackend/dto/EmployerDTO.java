package com.teamarc.careerlybackend.dto;

import com.teamarc.careerlybackend.entity.User;
import lombok.Data;

@Data
public class EmployerDTO {

    private Long employerId;
    private String companyName;
    private String companyWebsite;
    private User user;
}
