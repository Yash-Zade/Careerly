package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.*;
import com.teamarc.careerlybackend.entity.*;
import com.teamarc.careerlybackend.entity.enums.Role;
import com.teamarc.careerlybackend.exceptions.RuntimeConflictException;
import com.teamarc.careerlybackend.repository.OnboardNewEmployerRepository;
import com.teamarc.careerlybackend.repository.OnboardNewMentorRepository;
import com.teamarc.careerlybackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final EmployerService employerService;
    private final MentorService mentorService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final OnboardNewEmployerRepository onboardNewEmployerRepository;
    private final OnboardNewMentorRepository onboardNewMentorRepository;
    private final RabbitMQService rabbitMQService;

    @Transactional
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
        rabbitMQService.sendEmail(emailRequest);
        onboardNewEmployerRepository.delete(modelMapper.map(onBoardNewEmployerDTO, OnboardNewEmployer.class));
        return modelMapper.map(savedEmployer, EmployerDTO.class);
    }


    @Transactional
    public MentorProfileDTO onboardNewMentor(Long userId, OnboardNewMentorDTO onboardNewMentorDTO) {
        User user = userService.getUserById(userId);
        if (user.getRoles().contains(Role.MENTOR)) {
            throw new RuntimeConflictException("User with id: " + userId + " is already a mentor");
        }
        Mentor createMentor = Mentor.builder()
                .expertise(onboardNewMentorDTO.getExpertise())
                .user(user)
                .experience(onboardNewMentorDTO.getExperience())
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
        rabbitMQService.sendEmail(emailRequest);
        onboardNewMentorRepository.delete(modelMapper.map(onboardNewMentorDTO, OnboardNewMentor.class));

        return modelMapper.map(savedMentor, MentorProfileDTO.class);
    }

    public Long getTotalUsers() {
        return userRepository.count();
    }

    public Long getTotalEmployers() {
        return userRepository.countByRoles(Role.EMPLOYER);
    }

    public Long getTotalMentors() {
        return userRepository.countByRoles(Role.MENTOR);
    }

    public Page<OnBoardNewEmployerDTO> getEmployerRequests(PageRequest pageRequest ) {
        return onboardNewEmployerRepository.findAll(pageRequest)
                .map(onboardNewEmployer -> modelMapper.map(onboardNewEmployer, OnBoardNewEmployerDTO.class));
    }

    public Page<OnboardNewMentorDTO> getMentorRequests(PageRequest pageRequest) {
        return onboardNewMentorRepository.findAll(pageRequest).map((element) -> modelMapper.map(element, OnboardNewMentorDTO.class));
    }

    public void rejectEmployer(Long userId, OnBoardNewEmployerDTO onBoardNewEmployerDTO) {

        OnboardNewEmployer onBoardNewEmployer = modelMapper.map(onBoardNewEmployerDTO, OnboardNewEmployer.class);
        onboardNewEmployerRepository.deleteById(onBoardNewEmployer.getId());
    }

    public void rejectMentor(Long userId, OnboardNewMentorDTO onboardNewMentorDTO) {
        OnboardNewMentor onboardNewMentor = modelMapper.map(onboardNewMentorDTO, OnboardNewMentor.class);
        onboardNewMentorRepository.deleteById(onboardNewMentor.getId());
    }

    public Long getTotalRequests() {
        return onboardNewEmployerRepository.count() + onboardNewMentorRepository.count();
    }
}
