package com.icms.notificationservice.consumer;

import com.icms.notificationservice.config.RabbitMQConfig;
import com.icms.notificationservice.event.claim.ClaimApprovedEvent;
import com.icms.notificationservice.event.claim.ClaimCreatedEvent;
import com.icms.notificationservice.event.claim.ClaimRejectedEvent;
import com.icms.notificationservice.event.customer.CustomerCreatedEvent;
import com.icms.notificationservice.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ClaimEventConsumer {

    private final NotificationService notificationService;

    public ClaimEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = RabbitMQConfig.CLAIM_CREATED_QUEUE,
    containerFactory = "rabbitListenerContainerFactory")
    void handleClaimCreated(ClaimCreatedEvent claimCreatedEvent){
        notificationService.notifyClaimCreated(claimCreatedEvent);
    }

    @RabbitListener(queues = RabbitMQConfig.CLAIM_APPROVED_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    void handleClaimApproved(ClaimApprovedEvent claimApprovedEvent){
        notificationService.notifyClaimApproved(claimApprovedEvent);
    }

    @RabbitListener(queues = RabbitMQConfig.CLAIM_REJECTED_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    void handleClaimRejected(ClaimRejectedEvent claimRejectedEvent){
        notificationService.notifyClaimRejected(claimRejectedEvent);
    }


}
