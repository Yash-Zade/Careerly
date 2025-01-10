package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.*;
import com.teamarc.careerlybackend.entity.Employer;
import com.teamarc.careerlybackend.entity.Mentor;
import com.teamarc.careerlybackend.entity.User;
import com.teamarc.careerlybackend.entity.enums.Role;
import com.teamarc.careerlybackend.exceptions.RuntimeConflictException;
import com.teamarc.careerlybackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final EmployerService employerService;
    private final MentorService mentorService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final EmailSenderService emailSenderService;
    private final AmqpTemplate amqpTemplate;

    public EmployerDTO onboardNewEmployer(Long userId, OnBoardNewEmployerDTO onBoardNewEmployerDTO) {
        User user = userService.getUserById(userId);
        if (user.getRoles().contains(Role.EMPLOYER)) {
            throw new RuntimeConflictException("user with id: " + userId + " is already a employer");
        }
        Employer createEmployer = Employer.builder()
                .companyName(onBoardNewEmployerDTO.getCompanyName())
                .companyWebsite(onBoardNewEmployerDTO.getCompanyWebsite())
                .user(user)
                .build();
        user.getRoles().add(Role.EMPLOYER);
        userRepository.save(user);
        Employer savedEmployer = employerService.createNewEmployer(createEmployer);
        EmailRequest emailRequest = EmailRequest.builder()
                .toEmail(savedEmployer.getUser().getEmail())
                .subject("Welcome to Careerly")
                .body("Welcome to Careerly, you are now a registered employer")
                .buttonText("View")
                .buttonUrl("https://your-site.com")
                .build();
        amqpTemplate.convertAndSend("emailQueue", emailRequest);
        return modelMapper.map(savedEmployer, EmployerDTO.class);
    }


    public MentorProfileDTO onboardNewMentor(Long userId, OnboardNewMentorDTO onboardNewMentorDTO) {
        User user = userService.getUserById(userId);
        if (user.getRoles().contains(Role.MENTOR)) {
            throw new RuntimeConflictException("User with id: " + userId + " is already a mentor");
        }
        Mentor createMentor = Mentor.builder()
                .expertise(onboardNewMentorDTO.getExpertise())
                .user(user)
                .build();
        user.getRoles().add(Role.MENTOR);
        userRepository.save(user);
        Mentor savedMentor = mentorService.createNewMentor(createMentor);

        EmailRequest emailRequest = EmailRequest.builder()
                .toEmail(savedMentor.getUser().getEmail())
                .subject("Welcome to Careerly")
                .body("Welcome to Careerly, you are now a registered mentor")
                .buttonText("View")
                .buttonUrl("https://your-site.com")
                .build();

        amqpTemplate.convertAndSend("emailQueue", emailRequest);

        return modelMapper.map(savedMentor, MentorProfileDTO.class);
    }
}
