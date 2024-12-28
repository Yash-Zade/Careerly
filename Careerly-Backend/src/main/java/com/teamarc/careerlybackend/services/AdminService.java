package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.EmployerDTO;
import com.teamarc.careerlybackend.dto.MentorDTO;
import com.teamarc.careerlybackend.dto.OnBoardNewEmployerDTO;
import com.teamarc.careerlybackend.dto.OnboardNewMentorDTO;
import com.teamarc.careerlybackend.entity.Employer;
import com.teamarc.careerlybackend.entity.Mentor;
import com.teamarc.careerlybackend.entity.User;
import com.teamarc.careerlybackend.entity.enums.Role;
import com.teamarc.careerlybackend.exceptions.ResourceNotFoundException;
import com.teamarc.careerlybackend.exceptions.RuntimeConflictException;
import com.teamarc.careerlybackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final EmployerService employerService;
    private final MentorService mentorService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public EmployerDTO onboardNewEmployer(Long userId, OnBoardNewEmployerDTO onBoardNewEmployerDTO) {
        User user=userService.getUserById(userId);
        if(user.getRoles().contains(Role.EMPLOYER)){
            throw new RuntimeConflictException("user with id: "+userId+" is already a employer");
        }
        Employer createEmployer= Employer.builder()
                .companyName(onBoardNewEmployerDTO.getCompanyName())
                .companyWebsite(onBoardNewEmployerDTO.getCompanyWebsite())
                .user(user)
                .build();
        user.getRoles().add(Role.EMPLOYER);
        userRepository.save(user);
        Employer savedEmployer = employerService.createNewEmployer(createEmployer);
        return modelMapper.map(savedEmployer, EmployerDTO.class);
    }

    public MentorDTO onboardNewMentor(Long userId, OnboardNewMentorDTO onboardNewMentorDTO) {
        User user = userService.getUserById(userId);
        if (user.getRoles().contains(Role.MENTOR)) {
            throw new RuntimeConflictException("User with id: " + userId + " is already a mentor");
        }
        Mentor createMentor = Mentor.builder()
                .expertise(onboardNewMentorDTO.getExpertise())
                .ratings(null)
                .sessions(null)
                .user(user)
                .build();
        user.getRoles().add(Role.MENTOR);
        userRepository.save(user);
        Mentor savedMentor = mentorService.createNewMentor(createMentor);
        return modelMapper.map(savedMentor, MentorDTO.class);
    }
}
