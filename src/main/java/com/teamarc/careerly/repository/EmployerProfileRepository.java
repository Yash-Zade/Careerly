package com.teamarc.careerly.repository;

import com.teamarc.careerly.entities.EmployerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// EmployerProfileRepository.java
@Repository
public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, Long> {
    Optional<EmployerProfile> findByEmail(String email);
}
