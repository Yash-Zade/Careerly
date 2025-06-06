package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.entity.Payment;
import com.teamarc.careerlybackend.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findBySession(Session session);
}
