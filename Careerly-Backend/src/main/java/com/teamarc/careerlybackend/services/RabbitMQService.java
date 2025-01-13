package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RabbitMQService {

    private final AmqpTemplate amqpTemplate;


    public void sendEmail(EmailRequest emailRequest) {
        try {
            amqpTemplate.convertAndSend("emailQueue", emailRequest);
        }
        catch (Exception e) {
            System.out.println("Error in sending message: "+e.getMessage());
        }

    }
}
