package com.teamarc.careerlybackend.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employerId;

    private String companyName;
    private String companyWebsite;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
