package com.icms.policyservice.consumer;

import com.icms.policyservice.event.customer.CustomerActivatedEvent;
import com.icms.policyservice.event.customer.CustomerCreatedEvent;
import com.icms.policyservice.event.customer.CustomerDeactivatedEvent;
import com.icms.policyservice.service.CustomerCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CustomerEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(CustomerEventConsumer.class);

    private final CustomerCacheService customerCacheService;

    public CustomerEventConsumer(CustomerCacheService customerCacheService) {
        this.customerCacheService = customerCacheService;
    }

    @RabbitListener(queues = "customer.created.queue")
    public void handleCustomerCreated(CustomerCreatedEvent customerCreatedEvent){
        customerCacheService.addCustomerCache(customerCreatedEvent);
        logger.info("Consume CustomerCreatedEvent: {}",customerCreatedEvent);
    }

    @RabbitListener(queues = "customer.activated.queue")
    public void handleCustomerActivated(CustomerActivatedEvent customerActivatedEvent){
        customerCacheService.activateCustomer(customerActivatedEvent);
        logger.info("Consumed CustomerActivatedEvent: {}",customerActivatedEvent);
    }

    @RabbitListener(queues = "customer.deactivated.queue")
    public void handleCustomerDeactivated(CustomerDeactivatedEvent customerDeactivatedEvent){
        customerCacheService.deactivateCustomer(customerDeactivatedEvent);
        logger.info("Consumed CustomerDeactivatedEvent: {}",customerDeactivatedEvent);
    }

}
