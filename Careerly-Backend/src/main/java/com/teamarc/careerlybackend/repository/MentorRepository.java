package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.entity.Applicant;
import com.teamarc.careerlybackend.entity.Job;
import com.teamarc.careerlybackend.entity.Mentor;
import com.teamarc.careerlybackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {
    Optional<Mentor> findByUser(User user);
}
