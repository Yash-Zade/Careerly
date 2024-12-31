package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.dto.RatingDTO;
import com.teamarc.careerlybackend.entity.Mentor;
import com.teamarc.careerlybackend.entity.Rating;
import com.teamarc.careerlybackend.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {
    Optional<Rating> findBySession(Session session);

    List<Rating> findByMentor(Mentor mentor);

    Optional<RatingDTO> findBySession_SessionId(Long sessionId);
}
