package com.teamarc.careerlybackend.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicantId;

    private String resume;

    @ElementCollection
    private List<String> skills;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "applicant", fetch = FetchType.LAZY)
    private List<JobApplication> jobApplications;

    private Boolean isFirstSession;

    @ElementCollection
    private List<String> preferredLocations;

}

