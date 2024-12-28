package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.entity.Employer;
import com.teamarc.careerlybackend.repository.EmployerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployerService {
    private final EmployerRepository employerRepository;

    public Employer createNewEmployer(Employer employer) {
        return employerRepository.save(employer);
    }


}
