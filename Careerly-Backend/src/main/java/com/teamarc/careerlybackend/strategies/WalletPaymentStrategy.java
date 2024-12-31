package com.teamarc.careerlybackend.strategies;


import com.teamarc.careerlybackend.entity.Applicant;
import com.teamarc.careerlybackend.entity.Mentor;
import com.teamarc.careerlybackend.entity.Payment;
import com.teamarc.careerlybackend.entity.Wallet;
import com.teamarc.careerlybackend.entity.enums.PaymentStatus;
import com.teamarc.careerlybackend.repository.PaymentRepository;
import com.teamarc.careerlybackend.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@RequiredArgsConstructor
@Service
public class WalletPaymentStrategy {

    private final BigDecimal PLATFORM_COMMISSION = new BigDecimal("0.1");
    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

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

        walletService.deductMoneyToWallet(applicant.getUser(), payment.getAmount(), null, payment.getSession());

        BigDecimal driversCut = payment.getAmount().multiply(BigDecimal.ONE.subtract(PLATFORM_COMMISSION));
        walletService.addMoneyToWallet(mentor.getUser(), driversCut, null, payment.getSession());
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

        walletService.addMoneyToWallet(applicant.getUser(), payment.getAmount(), null, payment.getSession());
        walletService.deductMoneyToWallet(mentor.getUser(), payment.getAmount(), null, payment.getSession());
        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);
    }
}
