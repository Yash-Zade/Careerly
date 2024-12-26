package com.teamarc.careerlybackend.entity;

import com.teamarc.careerlybackend.entity.enums.CallStatus;
import com.teamarc.careerlybackend.entity.enums.CallType;
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
    private Long callId;

    @ManyToOne
    @JoinColumn(name = "caller_id")
    private User caller;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private CallType callType;
    private Date callStartTime;
    private Date callEndTime;
    private String googleMeetLink;  // For video calls

    private CallStatus status;


}

