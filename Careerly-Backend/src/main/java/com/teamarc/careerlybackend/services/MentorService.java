package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.entity.Employer;
import com.teamarc.careerlybackend.entity.Mentor;
import com.teamarc.careerlybackend.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;

    public Mentor createNewMentor(Mentor mentor) {
        return mentorRepository.save(mentor);
    }
}
