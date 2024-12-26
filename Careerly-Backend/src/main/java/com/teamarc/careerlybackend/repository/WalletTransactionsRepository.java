package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionsRepository extends JpaRepository<WalletTransaction, Long> {
}
