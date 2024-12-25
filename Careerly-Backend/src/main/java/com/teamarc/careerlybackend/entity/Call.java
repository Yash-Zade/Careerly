package com.teamarc.careerlybackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class Call {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String callId;

    @ManyToOne
    @JoinColumn(name = "caller_id")
    private User caller;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private String callType;  // "Audio" or "Video"
    private Date callStartTime;
    private Date callEndTime;
    private String googleMeetLink;  // For video calls



    // Getters and Setters
}

