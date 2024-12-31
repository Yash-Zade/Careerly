package com.teamarc.careerlybackend.entity;

import com.teamarc.careerlybackend.entity.enums.SessionStatus;
import com.teamarc.careerlybackend.entity.enums.SessionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private Long sessionId;

    private LocalDateTime sessionStartTime;
    private LocalDateTime sessionEndTime;
    private BigDecimal sessionFee;
    private String otp;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private Applicant applicant;

    @Enumerated(EnumType.STRING)
    private SessionType sessionType;

    @Enumerated(EnumType.STRING)
    private SessionStatus sessionStatus;

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL)
    private Rating rating;

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL)
    private Payment payment;

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL)
    private WalletTransaction walletTransaction;

    private String sessionLink;

}
