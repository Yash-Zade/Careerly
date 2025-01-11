package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.dto.OnBoardNewEmployerDTO;
import com.teamarc.careerlybackend.entity.OnboardNewEmployer;
import com.teamarc.careerlybackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnboardNewEmployerRepository extends JpaRepository<OnboardNewEmployer, Long> {
}