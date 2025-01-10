package com.teamarc.careerlybackend.services;


import com.teamarc.careerlybackend.entity.Payment;
import com.teamarc.careerlybackend.entity.Session;
import com.teamarc.careerlybackend.entity.enums.PaymentStatus;
import com.teamarc.careerlybackend.exceptions.ResourceNotFoundException;
import com.teamarc.careerlybackend.repository.PaymentRepository;
import com.teamarc.careerlybackend.strategies.WalletPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final WalletPaymentStrategy walletPaymentStrategy;
    private final PaymentRepository paymentRepository;

    public void processPayment(Session session) {
        Payment payment = paymentRepository.findBySession(session)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for session with id: " + session.getSessionId()));
        walletPaymentStrategy.processPayment(payment);
    }

    public void updatePaymentStatus(Payment payment, PaymentStatus status) {
        payment.setPaymentStatus(status);
        paymentRepository.save(payment);
    }

    public Payment createNewPayment(Session session) {
        Payment payment = Payment.builder()
                .session(session)
                .paymentStatus(PaymentStatus.PENDING)
                .amount(session.getSessionFee())
                .build();
        return paymentRepository.save(payment);
    }

    public void refundPayment(Session session) {
        Payment payment = paymentRepository.findBySession(session)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for session with id: " + session.getSessionId()));
        walletPaymentStrategy.refundPayment(payment);
    }
}