package com.icms.notificationservice.eventpublisher;

import com.icms.notificationservice.config.RabbitMQConfig;
import com.icms.notificationservice.event.email.SendEmailEvent;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmailEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public EmailEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEmailEvent(String toEmail, String subject, String body){
        SendEmailEvent sendEmailEvent = new SendEmailEvent(toEmail,subject,body);

        CorrelationData correlationData = new CorrelationData("email-event-" + subject);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EMAIL_EXCHANGE,
                RabbitMQConfig.EMAIL_ROUTING_KEY,
                sendEmailEvent,
                correlationData);
    }
}
