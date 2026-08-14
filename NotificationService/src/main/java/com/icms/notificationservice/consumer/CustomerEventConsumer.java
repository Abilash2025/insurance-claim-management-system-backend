package com.icms.notificationservice.consumer;

import com.icms.notificationservice.config.RabbitMQConfig;
import com.icms.notificationservice.event.customer.CustomerActivatedEvent;
import com.icms.notificationservice.event.customer.CustomerCreatedEvent;
import com.icms.notificationservice.event.customer.CustomerDeactivatedEvent;
import com.icms.notificationservice.service.CustomerCacheService;
import com.icms.notificationservice.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CustomerEventConsumer {

    private final NotificationService notificationService;
    private final CustomerCacheService customerCacheService;

    public CustomerEventConsumer(
            NotificationService notificationService,
            CustomerCacheService customerCacheService) {
        this.notificationService = notificationService;
        this.customerCacheService = customerCacheService;
    }

    @RabbitListener(queues = RabbitMQConfig.CUSTOMER_CREATED_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    void handleCustomerCreated(CustomerCreatedEvent customerCreatedEvent){
        customerCacheService.addCustomerCache(customerCreatedEvent);
        notificationService.notifyCustomerCreated(customerCreatedEvent);
    }

    @RabbitListener(queues = RabbitMQConfig.CUSTOMER_ACTIVATED_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    void handleCustomerActivated(CustomerActivatedEvent customerActivatedEvent){
        customerCacheService.activateCustomerCache(customerActivatedEvent);
        notificationService.notifyCustomerActivated(customerActivatedEvent);
    }

    @RabbitListener(queues = RabbitMQConfig.CUSTOMER_DEACTIVATED_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    void handleCustomerDeactivated(CustomerDeactivatedEvent customerDeactivatedEvent){
        customerCacheService.deactivateCustomerCache(customerDeactivatedEvent);
        notificationService.notifyCustomerDeactivated(customerDeactivatedEvent);
    }
}
