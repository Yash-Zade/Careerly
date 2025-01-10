package com.teamarc.careerlybackend.strategies;


import com.teamarc.careerlybackend.entity.*;
import com.teamarc.careerlybackend.entity.enums.PaymentStatus;
import com.teamarc.careerlybackend.entity.enums.Role;
import com.teamarc.careerlybackend.repository.PaymentRepository;
import com.teamarc.careerlybackend.services.UserService;
import com.teamarc.careerlybackend.services.WalletService;
import com.teamarc.careerlybackend.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class WalletPaymentStrategy {

    private final BigDecimal PLATFORM_COMMISSION = new BigDecimal("0.1");
    private final WalletService walletService;
    private final PaymentRepository paymentRepository;
    private final WalletTransactionService walletTransactionService;
    private final UserService userService;

    @Transactional
    public void processPayment(Payment payment) {

        Mentor mentor = payment.getSession().getMentor();
        Applicant applicant = payment.getSession().getApplicant();
        Wallet applicantWallet = walletService.findWalletById(applicant.getApplicantId());

        if (applicantWallet.getBalance().compareTo(payment.getAmount()) < 0) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new RuntimeException("Insufficient balance in wallet: payment failed");
        }

        walletService.deductMoneyToWallet(applicant.getUser(), payment.getAmount(), generateTransactionId(), payment.getSession());

        BigDecimal mentorCut = payment.getAmount().multiply(BigDecimal.ONE.subtract(PLATFORM_COMMISSION));
        BigDecimal platformCut = payment.getAmount().subtract(mentorCut);
        User admin = userService.loadUserByRole(Role.ADMIN);
        walletService.addMoneyToWallet(admin, platformCut, generateTransactionId(), payment.getSession());
        walletService.addMoneyToWallet(mentor.getUser(), mentorCut, generateTransactionId(), payment.getSession());
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);
    }

    @Transactional
    public void refundPayment(Payment payment) {
        Mentor mentor = payment.getSession().getMentor();
        Applicant applicant = payment.getSession().getApplicant();
        Wallet mentorWallet = walletService.findWalletById(mentor.getMentorId());

        if (mentorWallet.getBalance().compareTo(payment.getAmount()) < 0) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new RuntimeException("Insufficient balance in mentor's wallet: refund failed");
        }

        walletService.addMoneyToWallet(applicant.getUser(), payment.getAmount(), generateTransactionId(), payment.getSession());
        walletService.deductMoneyToWallet(mentor.getUser(), payment.getAmount(), generateTransactionId(), payment.getSession());
        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);
    }

    public String generateTransactionId() {
        String transactionId = "TX" + UUID.randomUUID().toString().replace("-", "");
        if (walletTransactionService.findByTransactionId(transactionId).isPresent()) {
            return generateTransactionId();
        }
        return transactionId;
    }
}
