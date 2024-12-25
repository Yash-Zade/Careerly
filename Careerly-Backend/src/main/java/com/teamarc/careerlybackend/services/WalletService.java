package com.teamarc.careerlybackend.services;


import com.teamarc.careerlybackend.dto.TransactionIdDTO;
import com.teamarc.careerlybackend.entity.*;
import com.teamarc.careerlybackend.entity.enums.PaymentStatus;
import com.teamarc.careerlybackend.entity.enums.Roles;
import com.teamarc.careerlybackend.entity.enums.TransactionType;
import com.teamarc.careerlybackend.exceptions.ResourceNotFoundException;
import com.teamarc.careerlybackend.repository.UserRepository;
import com.teamarc.careerlybackend.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService{

    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final UserRepository userRepository;

    @Transactional
    public void addMoneyToWallet(User user, BigDecimal amount, Long transactionId, Session session) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance().add(amount));
        Payment payment = Payment.builder()
                .paymentId(transactionId)
                .session(session)
                .wallet(wallet)
                .transactionType(TransactionType.CREDIT)
                .amount(amount)
                .status(PaymentStatus.COMPLETED)
                .build();

        paymentService.createNewWalletTransaction(payment);
        walletRepository.save(wallet);
    }

    @Transactional
    public Wallet deductMoneyToWallet(User user, BigDecimal sessionCharges, TransactionIdDTO transactionIdDTO, Session session) {
        Wallet wallet = findByUser(user);
        if (wallet.getBalance().compareTo(sessionCharges) < 0) {
            throw new IllegalArgumentException("Insufficient balance.");
        }

        wallet.setBalance(wallet.getBalance().subtract(sessionCharges));

        User admin = userRepository.findByRoles(Roles.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        BigDecimal adminCut = sessionCharges.multiply(BigDecimal.valueOf(0.1));
        BigDecimal mentorCut = sessionCharges.subtract(adminCut);

        Payment walletTransaction = Payment.builder()
                .paymentId(transactionIdDTO.getUserTransactionId())
                .session(session)
                .wallet(wallet)
                .transactionType(TransactionType.DEBIT)
                .adminCut(adminCut)
                .admin((Admin)admin)
                .amount(sessionCharges)
                .status(PaymentStatus.COMPLETED)
                .build();

        User mentor = session.getMentor();
        addMoneyToWallet(mentor, mentorCut, transactionIdDTO.getMentorTransactionId(), session);

        addMoneyToWallet(admin, adminCut, transactionIdDTO.getAdminTransactionId(), session);

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
