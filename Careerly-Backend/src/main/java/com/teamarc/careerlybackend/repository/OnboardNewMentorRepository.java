package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.entity.OnboardNewMentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnboardNewMentorRepository extends JpaRepository<OnboardNewMentor, Long> {
}