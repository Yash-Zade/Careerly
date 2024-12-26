package com.teamarc.careerlybackend.entity;

import com.teamarc.careerlybackend.entity.enums.SessionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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

    private String sessionStartTime;
    private String sessionEndTime;
    private Boolean isPaid;
    private Boolean isFirstSession;
    private BigDecimal sessionFee;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private Applicant applicant;

    private SessionType sessionType;

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL)
    private Rating rating;

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL)
    private Payment payment;

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL)
    private WalletTransaction walletTransaction;

}
