package com.teamarc.careerlybackend.services;

import com.teamarc.careerly.entities.*;
import com.teamarc.careerly.entities.enums.TransactionType;
import com.teamarc.careerly.exceptions.ResourceNotFoundException;
import com.teamarc.careerly.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletService{

    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    private final WalletTransactionService walletTransactionService;

    @Transactional
    public Wallet addMoneyToWallet(User user, Double amount, Long transactionId, Session session) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance()+amount);
        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .session(session)
                .wallet(wallet)
                .type(TransactionType.CREDIT)
                .amount(amount)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);
        return walletRepository.save(wallet);
    }

    @Transactional
    public Wallet deductMoneyToWallet(User user, Double amount, Long transactionId, Session session) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance()-amount);
        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .session(session)
                .wallet(wallet)
                .type(TransactionType.DEBIT)
                .amount(amount)
                .build();

        wallet.getTransactions().add(walletTransaction);
        return walletRepository.save(wallet);

    }

    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(()-> new ResourceNotFoundException("Wallet not found with id: "+walletId));
    }

    public Wallet cerateNewWallet(User user) {
        Wallet wallet=new Wallet();
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }

    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user)
                .orElseThrow(()-> new ResourceNotFoundException("Wallet not found with id: "+user.getId()));
    }
}
