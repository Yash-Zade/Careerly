package com.teamarc.careerlybackend.services;


import com.teamarc.careerlybackend.dto.ApplicantDTO;
import com.teamarc.careerlybackend.dto.MentorDTO;
import com.teamarc.careerlybackend.dto.SessionDTO;
import com.teamarc.careerlybackend.entity.Applicant;
import com.teamarc.careerlybackend.entity.Mentor;
import com.teamarc.careerlybackend.entity.Session;
import com.teamarc.careerlybackend.entity.enums.SessionStatus;
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
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SessionService {


    private final SessionRepository sessionRepository;
    private final ModelMapper modelMapper;
    private final MentorRepository mentorRepository;

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