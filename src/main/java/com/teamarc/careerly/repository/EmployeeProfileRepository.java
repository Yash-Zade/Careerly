package com.teamarc.careerly.repository;

import com.teamarc.careerly.entities.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {
    Optional<EmployeeProfile> findByEmail(String email);
}

