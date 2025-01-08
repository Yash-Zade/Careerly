package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.entity.Session;
import com.teamarc.careerlybackend.entity.User;
import com.teamarc.careerlybackend.entity.Wallet;
import com.teamarc.careerlybackend.entity.WalletTransaction;
import com.teamarc.careerlybackend.entity.enums.TransactionType;
import com.teamarc.careerlybackend.exceptions.ResourceNotFoundException;
import com.teamarc.careerlybackend.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionService walletTransactionService;
    private final EmailSenderService emailSenderService;


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
        emailSenderService.sendEmail(user.getEmail(), "Money Added to Wallet",
                "Money of amount: " + amount + " has been added to your wallet. Transaction Id: " + transactionId);
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
        emailSenderService.sendEmail(user.getEmail(), "Money Deducted from Wallet",
                "Money of amount: " + amount + " has been deducted from your wallet. Transaction Id: " + transactionId);
        return walletRepository.save(wallet);

    }

    public void withdrawAllMyMoneyFromWallet() {

    }

    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(()-> new ResourceNotFoundException("Wallet not found with id: "+walletId));
    }

    public Wallet createNewWallet(User user) {
        Wallet wallet=Wallet.builder()
                .balance(BigDecimal.ZERO)
                .user(user)
                .transactions(null)
                .build();
        return walletRepository.save(wallet);
    }

    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user)
                .orElseThrow(()-> new ResourceNotFoundException("Wallet not found with id: "+user.getId()));
    }
}
