package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.entity.User;
import com.teamarc.careerlybackend.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import com.teamarc.careerlybackend.entity.enums.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    User findByRoles(Role role);
}

