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
public class Mentor extends User {

    @ElementCollection
    private List<String> expertise;

    @OneToMany(mappedBy = "mentor")
    private List<Session> sessions;

    // Getters and Setters
}

