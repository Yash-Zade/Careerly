package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.entity.Session;
import com.teamarc.careerlybackend.entity.User;
import com.teamarc.careerlybackend.entity.Wallet;
import com.teamarc.careerlybackend.entity.WalletTransaction;
import com.teamarc.careerlybackend.entity.enums.TransactionType;
import com.teamarc.careerlybackend.exceptions.ResourceNotFoundException;
import com.teamarc.careerlybackend.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    private final WalletTransactionService walletTransactionService;

    @Transactional
    public Wallet addMoneyToWallet(User user, BigDecimal amount, String transactionId, Session session) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance().add(amount));
        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .session(session)
                .wallet(wallet)
                .transactionType(TransactionType.CREDIT)
                .amount(amount)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);
        return walletRepository.save(wallet);
    }

    @Transactional
    public Wallet deductMoneyToWallet(User user, BigDecimal amount, String transactionId, Session session) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance().subtract(amount));
        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .session(session)
                .wallet(wallet)
                .transactionType(TransactionType.DEBIT)
                .amount(amount)
                .build();

        wallet.getTransactions().add(walletTransaction);
        return walletRepository.save(wallet);

    }

    public void withdrawAllMyMoneyFromWallet() {

    }

    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with id: " + walletId));
    }

    public Wallet cerateNewWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }

    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with id: " + user.getUserId()));
    }
}
