package com.icms.policyservice.eventpublisher;

import com.icms.policyservice.config.RabbitMQConfig;
import com.icms.policyservice.entity.PolicyEntity;
import com.icms.policyservice.event.policy.PolicyActivatedEvent;
import com.icms.policyservice.event.policy.PolicyCreatedEvent;
import com.icms.policyservice.event.policy.PolicyDeactivatedEvent;
import com.icms.policyservice.event.policy.PolicyUpdatedEvent;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class PolicyEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public PolicyEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPolicyCreatedEvent(PolicyEntity policyEntity){
        PolicyCreatedEvent policyCreatedEvent = new PolicyCreatedEvent();
        BeanUtils.copyProperties(policyEntity,policyCreatedEvent);
        policyCreatedEvent.setStatus(policyEntity.getStatus());

        CorrelationData correlationData = new CorrelationData("policy-created-" + policyEntity.getPolicyNumber());

        rabbitTemplate.convertAndSend(RabbitMQConfig.POLICY_EXCHANGE,
                RabbitMQConfig.POLICY_CREATED_ROUTING_KEY,
                policyCreatedEvent,
                correlationData);
    }

    public void publishPolicyUpdatedEvent(PolicyEntity policyEntity){
        PolicyUpdatedEvent policyUpdatedEvent = new PolicyUpdatedEvent();
        BeanUtils.copyProperties(policyEntity,policyUpdatedEvent);

        CorrelationData correlationData = new CorrelationData("policy-updated-" + policyEntity.getPolicyNumber());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.POLICY_EXCHANGE,
                RabbitMQConfig.POLICY_UPDATED_ROUTING_KEY,
                policyUpdatedEvent,
                correlationData
        );
    }

    public void publishPolicyDeactivatedEvent(PolicyEntity policyEntity){
        PolicyDeactivatedEvent policyDeactivatedEvent = new PolicyDeactivatedEvent(
                policyEntity.getPolicyNumber(),
                policyEntity.getStatus()
        );

        CorrelationData correlationData = new CorrelationData("policy-deactivated-" + policyEntity.getPolicyNumber());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.POLICY_EXCHANGE,
                RabbitMQConfig.POLICY_DEACTIVATED_ROUTING_KEY,
                policyDeactivatedEvent,
                correlationData
        );
    }

    public void publishPolicyActivatedEvent(PolicyEntity policyEntity){
        PolicyActivatedEvent policyActivatedEvent = new PolicyActivatedEvent(
                policyEntity.getPolicyNumber(),
                policyEntity.getStatus()
        );

        CorrelationData correlationData = new CorrelationData("policy-activated-" + policyEntity.getPolicyNumber());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.POLICY_EXCHANGE,
                RabbitMQConfig.POLICY_ACTIVATED_ROUTING_KEY,
                policyActivatedEvent,
                correlationData
        );

    }
}
