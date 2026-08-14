package com.icms.emailservice.service;

import com.icms.emailservice.event.email.SendEmailEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendMail(SendEmailEvent event) {
        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(event.getTo());
            helper.setSubject(event.getSubject());
            helper.setText(event.getBody(), true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send email", e);
        }
    }
}
