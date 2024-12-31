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
public class Mentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mentorId;

    @ElementCollection
    private List<String> expertise;

    @OneToMany(mappedBy = "mentor")
    private List<Session> sessions;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "mentor")
    private List<Rating> ratings;

    private Double averageRating;
}

