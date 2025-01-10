package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final EmailSenderService emailSenderService;

    @RabbitListener(queues = "emailQueue")
    public void listenToEmailQueue(EmailRequest emailRequest) {
        emailSenderService.sendEmail(
                emailRequest.getToEmail(),
                emailRequest.getSubject(),
                emailRequest.getBody(),
                emailRequest.getButtonText(),
                emailRequest.getButtonUrl()
        );
    }
}
