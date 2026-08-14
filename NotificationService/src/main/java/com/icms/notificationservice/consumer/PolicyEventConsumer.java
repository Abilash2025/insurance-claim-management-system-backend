package com.icms.notificationservice.consumer;

import com.icms.notificationservice.config.RabbitMQConfig;
import com.icms.notificationservice.event.policy.PolicyActivatedEvent;
import com.icms.notificationservice.event.policy.PolicyCreatedEvent;
import com.icms.notificationservice.event.policy.PolicyDeactivatedEvent;
import com.icms.notificationservice.event.policy.PolicyUpdatedEvent;
import com.icms.notificationservice.service.NotificationService;
import com.icms.notificationservice.service.PolicyCacheService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class PolicyEventConsumer {

    private final NotificationService notificationService;
    private final PolicyCacheService policyCacheService;

    public PolicyEventConsumer(
            NotificationService notificationService,
            PolicyCacheService policyCacheService) {
        this.notificationService = notificationService;
        this.policyCacheService = policyCacheService;
    }

    @RabbitListener(queues = RabbitMQConfig.POLICY_CREATED_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    void handlePolicyCreated(PolicyCreatedEvent policyCreatedEvent){
        policyCacheService.addPolicyCache(policyCreatedEvent);
        notificationService.notifyPolicyCreated(policyCreatedEvent);
    }

    @RabbitListener(queues = RabbitMQConfig.POLICY_UPDATED_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    void handlePolicyUpdated(PolicyUpdatedEvent policyUpdatedEvent){
        policyCacheService.updatePolicyCache(policyUpdatedEvent);
        notificationService.notifyPolicyUpdated(policyUpdatedEvent);
    }

    @RabbitListener(queues = RabbitMQConfig.POLICY_ACTIVATED_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    void handlePolicyActivated(PolicyActivatedEvent policyActivatedEvent){
        policyCacheService.activatePolicyCache(policyActivatedEvent);
        notificationService.notifyPolicyActivated(policyActivatedEvent);
    }

    @RabbitListener(queues = RabbitMQConfig.POLICY_DEACTIVATED_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    void handlePolicyDeactivated(PolicyDeactivatedEvent policyDeactivatedEvent){
        policyCacheService.deactivatePolicyCache(policyDeactivatedEvent);
        notificationService.notifyPolicyDeactivated(policyDeactivatedEvent);
    }
}
