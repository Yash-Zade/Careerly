package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.entity.Employer;
import com.teamarc.careerlybackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {
    Optional<Employer> findByUser(User user);
}
