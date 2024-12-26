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

    @ManyToMany(mappedBy = "applications")
    private List<Job> appliedJobs;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}

