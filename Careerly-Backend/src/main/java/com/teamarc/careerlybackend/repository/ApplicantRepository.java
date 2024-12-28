package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.entity.Applicant;
import com.teamarc.careerlybackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ApplicantRepository extends JpaRepository<Applicant,Long> {
    Optional<Applicant> findByUser(User user);
}

