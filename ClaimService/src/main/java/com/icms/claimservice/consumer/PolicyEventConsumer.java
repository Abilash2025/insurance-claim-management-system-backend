package com.icms.claimservice.consumer;

import com.icms.claimservice.event.policy.PolicyActivatedEvent;
import com.icms.claimservice.event.policy.PolicyCreatedEvent;
import com.icms.claimservice.event.policy.PolicyDeactivatedEvent;
import com.icms.claimservice.event.policy.PolicyUpdatedEvent;
import com.icms.claimservice.exception.ResourceNotFoundException;
import com.icms.claimservice.service.PolicyCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class PolicyEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PolicyEventConsumer.class);

    private final PolicyCacheService policyCacheService;

    public PolicyEventConsumer(PolicyCacheService policyCacheService) {
        this.policyCacheService = policyCacheService;
    }

    @RabbitListener(queues = "policy.created.queue")
    void handlePolicyCreated(PolicyCreatedEvent event){
        policyCacheService.addPolicyCache(event);
        logger.info("Consumed PolicyCreatedEvent: {}",event);
    }

    @RabbitListener(queues = "policy.updated.queue")
    void handlePolicyUpdated(PolicyUpdatedEvent event)
            throws ResourceNotFoundException {
        policyCacheService.updatePolicyCache(event);
        logger.info("Consumed PolicyUpdatedEvent: {}",event);
    }

    @RabbitListener(queues = "policy.activated.queue")
    void handlePolicyActivated(PolicyActivatedEvent event) throws ResourceNotFoundException {
        policyCacheService.activatePolicyCache(event);
        logger.info("Consumed PolicyActivatedEvent: {}",event);
    }

    @RabbitListener(queues = "policy.deactivated.queue")
    void handlePolicyDeactivated(PolicyDeactivatedEvent event) throws ResourceNotFoundException {
        policyCacheService.deactivatePolicyCache(event);
        logger.info("Consumed PolicyDeactivatedEvent: {}", event);
    }


}
