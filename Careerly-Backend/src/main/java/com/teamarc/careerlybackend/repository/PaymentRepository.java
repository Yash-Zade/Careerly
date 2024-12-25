package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
