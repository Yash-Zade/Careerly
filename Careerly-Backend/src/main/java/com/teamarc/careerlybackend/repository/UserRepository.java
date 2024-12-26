package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.entity.User;
import com.teamarc.careerlybackend.entity.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);

    Optional<User> findByRoles(Roles roles);
}
