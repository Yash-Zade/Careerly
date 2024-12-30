package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.EmployerDTO;
import com.teamarc.careerlybackend.dto.MentorDTO;
import com.teamarc.careerlybackend.entity.Employer;
import com.teamarc.careerlybackend.entity.Mentor;
import com.teamarc.careerlybackend.entity.User;
import com.teamarc.careerlybackend.exceptions.ResourceNotFoundException;
import com.teamarc.careerlybackend.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;
    private final ModelMapper modelMapper;

    public Mentor createNewMentor(Mentor mentor) {
        return mentorRepository.save(mentor);
    }

    public MentorDTO getMyProfile() {
        Mentor mentor = getCurrentEmployer();
        return modelMapper.map(mentor, MentorDTO.class);
    }

    private Mentor getCurrentEmployer() {
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
}
