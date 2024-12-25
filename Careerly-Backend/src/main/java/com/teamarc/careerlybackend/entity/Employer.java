package com.teamarc.careerlybackend.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class Employer extends User {

    private String companyName;
    private String companyWebsite;

    @OneToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;

    // Getters and Setters
}
