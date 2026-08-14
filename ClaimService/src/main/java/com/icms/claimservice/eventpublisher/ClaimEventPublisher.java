package com.icms.claimservice.eventpublisher;

import com.icms.claimservice.config.RabbitMQConfig;
import com.icms.claimservice.entity.ClaimEntity;
import com.icms.claimservice.event.claim.ClaimApprovedEvent;
import com.icms.claimservice.event.claim.ClaimCreatedEvent;
import com.icms.claimservice.event.claim.ClaimRejectedEvent;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ClaimEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ClaimEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishClaimCreatedEvent(ClaimEntity claimEntity){

        ClaimCreatedEvent claimCreatedEvent = new ClaimCreatedEvent();
        BeanUtils.copyProperties(claimEntity,claimCreatedEvent);
        claimCreatedEvent.setStatus(claimEntity.getClaimStatus());

        CorrelationData correlationData = new CorrelationData("claim-created-" + claimEntity.getClaimNumber());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CLAIM_EXCHANGE,
                RabbitMQConfig.CLAIM_CREATED_ROUTING_KEY,
                claimCreatedEvent,
                correlationData
        );
    }

    public void publishClaimApprovedEvent(ClaimEntity claimEntity){
        ClaimApprovedEvent claimApprovedEvent = new ClaimApprovedEvent(
                claimEntity.getClaimNumber(),
                claimEntity.getPolicyNumber(),
                claimEntity.getClaimStatus()
        );

        CorrelationData correlationData = new CorrelationData("claim-approved-" + claimEntity.getClaimNumber());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CLAIM_EXCHANGE,
                RabbitMQConfig.CLAIM_APPROVED_ROUTING_KEY,
                claimApprovedEvent,
                correlationData
        );
    }

    public void publishClaimRejectedEvent(ClaimEntity claimEntity){
        ClaimRejectedEvent claimRejectedEvent = new ClaimRejectedEvent(
                claimEntity.getClaimNumber(),
                claimEntity.getPolicyNumber(),
                claimEntity.getClaimStatus()
        );

        CorrelationData correlationData = new CorrelationData("claim-rejected-" + claimEntity.getClaimNumber());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CLAIM_EXCHANGE,
                RabbitMQConfig.CLAIM_REJECTED_ROUTING_KEY,
                claimRejectedEvent,
                correlationData
        );

    }
}
