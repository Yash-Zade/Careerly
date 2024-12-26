package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.entity.User;
import com.teamarc.careerlybackend.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUser(User user);
}
