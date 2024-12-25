package com.teamarc.careerlybackend.services;


import com.teamarc.careerly.entities.WalletTransaction;
import com.teamarc.careerly.repository.WalletTransactionsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WalletTransactionService{

    private final ModelMapper modelMapper;
    private final WalletTransactionsRepository walletTransactionsRepository;

    public void createNewWalletTransaction(WalletTransaction walletTransaction) {
        walletTransactionsRepository.save(walletTransaction);
    }
}
