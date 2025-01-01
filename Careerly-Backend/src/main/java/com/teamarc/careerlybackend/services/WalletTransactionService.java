package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.entity.WalletTransaction;
import com.teamarc.careerlybackend.repository.WalletTransactionsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class WalletTransactionService {

    private final WalletTransactionsRepository walletTransactionsRepository;

    public void createNewWalletTransaction(WalletTransaction walletTransaction) {
        walletTransactionsRepository.save(walletTransaction);
    }

    public Optional<WalletTransaction> findByTransactionId(String transactionId) {
        return walletTransactionsRepository.findByTransactionId(transactionId);
    }
}
