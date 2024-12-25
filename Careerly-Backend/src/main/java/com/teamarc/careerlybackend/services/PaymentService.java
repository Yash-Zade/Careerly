package com.teamarc.careerlybackend.services;


import com.teamarc.careerlybackend.entity.Payment;
import com.teamarc.careerlybackend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final ModelMapper modelMapper;
    private final PaymentRepository paymentRepository;

    public void createNewWalletTransaction(Payment payment) {
        paymentRepository.save(payment);
    }
}
