package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.MentorDTO;
import com.teamarc.careerlybackend.dto.RatingDTO;
import com.teamarc.careerlybackend.dto.SessionDTO;
import com.teamarc.careerlybackend.entity.Mentor;
import com.teamarc.careerlybackend.entity.User;
import com.teamarc.careerlybackend.exceptions.ResourceNotFoundException;
import com.teamarc.careerlybackend.repository.MentorRepository;
import com.teamarc.careerlybackend.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;
    private final ModelMapper modelMapper;
    private final SessionService sessionService;
    private final SessionManagementService sessionManagementService;
    private final RatingService ratingService;
    private final RatingRepository ratingRepository;

    public Mentor createNewMentor(Mentor mentor) {
        return mentorRepository.save(mentor);
    }

    public MentorDTO getMyProfile() {
        Mentor mentor = getCurrentMentor();
        return modelMapper.map(mentor, MentorDTO.class);
    }

    private Mentor getCurrentMentor() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return mentorRepository.findByUser(user)
                .orElseThrow(()-> new ResourceNotFoundException("Applicant not associated with user with id: "+ user.getId()));

    }

    public MentorDTO getProfileById(Long id) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with id: " + id));
        return modelMapper.map(mentor, MentorDTO.class);
    }

    public MentorDTO updateProfile(Long id, Map<String, Object> object) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with id: " + id));
        object.forEach((key, value) -> {
            Field field = ReflectionUtils.findRequiredField(Mentor.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, mentor, value);
        });
        Mentor updatedMentor = mentorRepository.save(mentor);
        return modelMapper.map(updatedMentor, MentorDTO.class);
    }

    public Page<SessionDTO> getSessions(Integer pageOffset, Integer pageSize) {
        return sessionService.getSessions(pageOffset, pageSize);
    }

    public SessionDTO getSessionById(Long id) {
        return sessionService.getSessionById(id);
    }

    public SessionDTO createSession(SessionDTO session) {
        return sessionService.createSession(session);
    }

    public SessionDTO updateSession(Long id, Map<String, Object> object) {
        return sessionService.updateSession(id, object);
    }

    public SessionDTO acceptSession(Long id) {
        return sessionManagementService.acceptSession(id);
    }

    public SessionDTO cancelSession(Long sessionId) {
        return sessionManagementService.cancelSession(sessionId);
    }


    public SessionDTO endSession(Long sessionId) {
        return sessionManagementService.endSessionByMentor(sessionId);
    }

    public SessionDTO startSession(Long sessionId) {
        return sessionManagementService.startSession(sessionId);
    }

    public RatingDTO rateMentor(Long sessionId) {
        return ratingRepository.findBySession_SessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found for session with id: " + sessionId));
    }


    public Page<RatingDTO> getRatings(Integer pageOffset, Integer pageSize) {
        return ratingService.getRatings(pageOffset, pageSize);
    }

    public Double getMyRating() {
        Mentor mentor = getCurrentMentor();
        return mentor.getAverageRating();
    }

    public boolean isOwnerOfSession(Long sessionId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SessionDTO session = sessionService.getSessionById(sessionId);
        MentorDTO mentor = getProfileById(session.getMentorId());
        User sessionUser = modelMapper.map(mentor.getUser(), User.class);
        return user.equals(sessionUser);
    }

    public boolean isOwnerOfProfile(Long mentorId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MentorDTO mentor = getProfileById(mentorId);
        User mentorUser = modelMapper.map(mentor.getUser(), User.class);
        return user.equals(mentorUser);
    }
}
