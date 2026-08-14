package com.icms.emailservice.consumer;

import com.icms.emailservice.config.RabbitMQConfig;
import com.icms.emailservice.event.email.SendEmailEvent;
import com.icms.emailservice.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EmailEventConsumer {

    private final EmailService emailService;

    public EmailEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    void handleSendEmail(SendEmailEvent event){
        emailService.sendMail(event);
    }
}
