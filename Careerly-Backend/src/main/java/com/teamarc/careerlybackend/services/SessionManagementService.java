package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.ApplicantDTO;
import com.teamarc.careerlybackend.dto.EmailRequest;
import com.teamarc.careerlybackend.dto.SessionDTO;
import com.teamarc.careerlybackend.entity.Applicant;
import com.teamarc.careerlybackend.entity.Session;
import com.teamarc.careerlybackend.entity.enums.SessionStatus;
import com.teamarc.careerlybackend.exceptions.ResourceNotFoundException;
import com.teamarc.careerlybackend.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SessionManagementService {

    private final SessionRepository sessionRepository;
    private final ModelMapper modelMapper;
    private final RatingService ratingService;
    private final PaymentService paymentService;
    private final AmqpTemplate amqpTemplate;
    private final RabbitMQService rabbitMQService;


    public SessionDTO startSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + sessionId));
        if (session.getSessionStatus().name().equals("SCHEDULED")) {
            session.setSessionStatus(SessionStatus.ONGOING);
            session.setSessionStartTime(LocalDateTime.now());
            Session savedSession = sessionRepository.save(session);
            return modelMapper.map(savedSession, SessionDTO.class);
        }
        throw new RuntimeException("Session not found with Id: " + session.getSessionId());
    }

    public SessionDTO joinSession(Long sessionId, String otp) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + sessionId));
        if (session.getSessionStatus().name().equals("SCHEDULED") && session.getOtp().equals(otp)) {
            session.setSessionStatus(SessionStatus.ONGOING);
            session.setSessionStartTime(LocalDateTime.now());
            Session savedSession = sessionRepository.save(session);
            return modelMapper.map(savedSession, SessionDTO.class);
        }
        throw new RuntimeException("Session not found with Id: " + session.getSessionId());

    }

    public SessionDTO endSessionByApplicant(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + sessionId));
        if (session.getSessionStatus().name().equals("ONGOING")) {
            session.setSessionStatus(SessionStatus.COMPLETED);
            session.setSessionEndTime(LocalDateTime.now());
            Session savedSession = sessionRepository.save(session);
            return modelMapper.map(savedSession, SessionDTO.class);
        }
        throw new RuntimeException("Session not found with Id: " + session.getSessionId());
    }


    public SessionDTO endSessionByMentor(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + sessionId));
        if (session.getSessionStatus().name().equals("SCHEDULED")) {
            session.setSessionStatus(SessionStatus.COMPLETED);
            session.setSessionEndTime(LocalDateTime.now());
            session.getMentor().setTotalSessions(session.getMentor().getTotalSessions() + 1);
            Session savedSession = sessionRepository.save(session);
            return modelMapper.map(savedSession, SessionDTO.class);
        }
        throw new RuntimeException("Session not found with Id: " + session.getSessionId());
    }


    public SessionDTO cancelSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not associated with Id: " + sessionId));

        if (!(session.getSessionStatus().name().equals("APPLIED") || session.getSessionStatus().name().equals("SCHEDULED"))) {
            throw new RuntimeException("Session not found with Id: " + session.getSessionId());
        }
        session.setSessionStatus(SessionStatus.CANCELLED);
        Session savedSession = sessionRepository.save(session);
        paymentService.refundPayment(savedSession);
        EmailRequest emailRequest = EmailRequest.builder()
                .toEmail(session.getApplicant().getUser().getEmail())
                .subject("Session Cancelled")
                .body("Your session has been cancelled by the mentor")
                .buttonText("View Profile")
                .buttonUrl("http://localhost:8080")
                .build();

        EmailRequest emailRequest1 = EmailRequest.builder()
                .toEmail(session.getMentor().getUser().getEmail())
                .subject("Session Cancelled")
                .body("Your session has been cancelled by the applicant")
                .buttonText("View Profile")
                .buttonUrl("http://localhost:8080")
                .build();

        rabbitMQService.sendEmail(emailRequest);
        rabbitMQService.sendEmail(emailRequest1);
        return modelMapper.map(session, SessionDTO.class);

    }

    public SessionDTO requestSession(Long sessionId, ApplicantDTO applicant) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + sessionId));
        session.setSessionStatus(SessionStatus.APPLIED);
        session.setApplicant(modelMapper.map(applicant, Applicant.class));
        Session savedSession = sessionRepository.save(session);
        paymentService.createNewPayment(session);

        EmailRequest emailRequest = EmailRequest.builder()
                .toEmail(session.getMentor().getUser().getEmail())
                .subject("Session Requested")
                .body("You have a new session request from " + applicant.getUser().getName())
                .buttonText("View Profile")
                .buttonUrl("http://localhost:8080")
                .build();

        EmailRequest emailRequest1 = EmailRequest.builder()
                .toEmail(applicant.getUser().getEmail())
                .subject("Session Requested")
                .body("Your session request has been sent to " + session.getMentor().getUser().getName())
                .buttonText("View Profile")
                .buttonUrl("http://localhost:8080")
                .build();

        rabbitMQService.sendEmail(emailRequest);
        rabbitMQService.sendEmail(emailRequest1);


        return modelMapper.map(savedSession, SessionDTO.class);
    }


    public SessionDTO acceptSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + sessionId));
        if (session.getSessionStatus().name().equals("APPLIED")) {
            session.setSessionStatus(SessionStatus.SCHEDULED);
            session.setOtp(generateRandomOtp());
            Session savedSession = sessionRepository.save(session);
            paymentService.processPayment(session);
            ratingService.creatNewRating(session);

            EmailRequest emailRequest = EmailRequest.builder()
                    .toEmail(session.getApplicant().getUser().getEmail())
                    .subject("Session Accepted")
                    .body("Your session has been accepted by the mentor, OTP: " + session.getOtp())
                    .buttonText("View Profile")
                    .buttonUrl("http://localhost:8080")
                    .build();
            rabbitMQService.sendEmail(emailRequest);
            return modelMapper.map(savedSession, SessionDTO.class);
        }
        throw new RuntimeException("Session not found with Id: " + session.getSessionId());
    }

    private String generateRandomOtp() {
        Random random = new Random();
        int otp = random.nextInt(10000);
        return String.format("%04d", otp);
    }


}

