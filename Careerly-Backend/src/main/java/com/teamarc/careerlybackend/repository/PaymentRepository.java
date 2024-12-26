package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.entity.Payment;
import com.teamarc.careerlybackend.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findBySession(Session session);
}
