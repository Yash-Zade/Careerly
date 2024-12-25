package com.teamarc.careerlybackend.entity;


import lombok.*;
import org.springframework.boot.autoconfigure.batch.BatchProperties;

import jakarta.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class Applicant extends User {

    private String resume;

    @ManyToMany(mappedBy = "applications")
    private List<Job> appliedJobs;

    // Getters and Setters
}

