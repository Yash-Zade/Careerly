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
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    private Integer ratingValue;  // 1-5
    private String comment;

    @OneToOne
    @JoinColumn(name = "session_id")
    private Session session;

    // Getters and Setters
}

