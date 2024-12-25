package com.teamarc.careerlybackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String sessionId;

    private String sessionStartTime;
    private String sessionEndTime;
    private Boolean isPaid;
    private Boolean isFirstSession;
    private Float sessionFee;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private Applicant applicant;

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL)
    private Rating rating;

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL)
    private Payment payment;

    // Getters and Setters
}
