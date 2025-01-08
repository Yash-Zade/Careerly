package com.teamarc.careerlybackend.services;


import com.teamarc.careerlybackend.dto.SessionDTO;
import com.teamarc.careerlybackend.entity.Session;
import com.teamarc.careerlybackend.exceptions.ResourceNotFoundException;
import com.teamarc.careerlybackend.repository.MentorRepository;
import com.teamarc.careerlybackend.repository.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SessionService {


    private final SessionRepository sessionRepository;
    private final ModelMapper modelMapper;
    private final MentorRepository mentorRepository;
    private final EmailSenderService emailSenderService;

    public Page<SessionDTO> getSessions(Integer pageOffset, Integer pageSize) {
        return sessionRepository.findAll(PageRequest.of(pageOffset, pageSize))
                .map(session -> modelMapper.map(session, SessionDTO.class));
    }

    public SessionDTO getSessionById(Long id) {
        return sessionRepository.findById(id)
                .map(session -> modelMapper.map(session, SessionDTO.class))
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    @Transactional
    public SessionDTO createSession(SessionDTO sessionDTO) {
        Session session = modelMapper.map(sessionDTO, Session.class);
        session.setMentor(mentorRepository.findById(sessionDTO.getMentorId())
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with id: " + sessionDTO.getMentorId())));
        Session savedSession = sessionRepository.save(session);
        emailSenderService.sendEmail(session.getMentor().getUser().getEmail(),
                "Session Created",
                "Session created successfully");
        return modelMapper.map(savedSession, SessionDTO.class);
    }

    public SessionDTO updateSession(Long id, Map<String, Object> object) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + id));
        object.forEach((key, value) -> {
            Field field = ReflectionUtils.findRequiredField(Session.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, session, value);
        });
        Session updatedSession = sessionRepository.save(session);
        return modelMapper.map(updatedSession, SessionDTO.class);
    }



}
