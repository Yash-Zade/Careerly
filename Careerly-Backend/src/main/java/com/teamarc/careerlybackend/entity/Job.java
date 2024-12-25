package com.teamarc.careerlybackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String jobId;

    private String title;
    private String description;
    private String location;

    @ElementCollection
    private List<String> skillsRequired;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Employer postedBy;

    @ManyToMany
    @JoinTable(
            name = "job_applications",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "applicant_id")
    )
    private List<Applicant> applications;

    private String status;  // "Open" or "Closed"

    // Getters and Setters
}