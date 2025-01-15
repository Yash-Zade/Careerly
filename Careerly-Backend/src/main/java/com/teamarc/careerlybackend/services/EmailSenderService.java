package com.teamarc.careerlybackend.services;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailSenderService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public void sendEmail(String toEmail, String subject, String body,
                          String buttonText, String buttonUrl) {
        try {
            Context context = new Context();
            context.setVariable("subject", subject);
            context.setVariable("content", body);

            // Add button if URL and text are provided
            if (buttonUrl != null && buttonText != null) {
                context.setVariable("hasButton", true);
                context.setVariable("buttonText", buttonText);
                context.setVariable("buttonUrl", buttonUrl);
            } else {
                context.setVariable("hasButton", false);
            }

            String htmlContent = templateEngine.process("email-template.html", context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            log.info("Email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Error sending email to: {}", toEmail, e);
        }
    }

}