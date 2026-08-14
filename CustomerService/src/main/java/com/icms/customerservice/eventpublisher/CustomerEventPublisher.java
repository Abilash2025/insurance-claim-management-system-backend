package com.icms.customerservice.eventpublisher;

import com.icms.customerservice.config.RabbitMQConfig;
import com.icms.customerservice.entity.CustomerEntity;
import com.icms.customerservice.event.CustomerActivatedEvent;
import com.icms.customerservice.event.CustomerCreatedEvent;
import com.icms.customerservice.event.CustomerDeactivatedEvent;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public CustomerEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCustomerCreatedEvent(CustomerEntity customerEntity){
        CorrelationData correlationData = new CorrelationData("customer-created-" + customerEntity.getCustomerId());
        CustomerCreatedEvent customerCreatedEvent = new CustomerCreatedEvent();
        BeanUtils.copyProperties(customerEntity,customerCreatedEvent);
        customerCreatedEvent.setStatus(customerEntity.getCustomerStatus());

        System.out.println(customerCreatedEvent);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CUSTOMER_EXCHANGE,
                RabbitMQConfig.CUSTOMER_CREATED_ROUTING_KEY,
                customerCreatedEvent,
                correlationData
        );
    }

    public void publishCustomerDeactivatedEvent(CustomerEntity customerEntity){
        CorrelationData correlationData = new CorrelationData("customer-deactivated-" + customerEntity.getCustomerId());

        CustomerDeactivatedEvent customerDeactivatedEvent = new CustomerDeactivatedEvent(
                customerEntity.getCustomerId(),
                customerEntity.getCustomerStatus()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CUSTOMER_EXCHANGE,
                RabbitMQConfig.CUSTOMER_DEACTIVATED_ROUTING_KEY,
                customerDeactivatedEvent,
                correlationData
        );
    }

    public void publishCustomerActivatedEvent(CustomerEntity customerEntity){
        CorrelationData correlationData = new CorrelationData("customer-activated-" + customerEntity.getCustomerId());

        CustomerActivatedEvent customerActivatedEvent = new CustomerActivatedEvent(
                customerEntity.getCustomerId(),
                customerEntity.getCustomerStatus()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CUSTOMER_EXCHANGE,
                RabbitMQConfig.CUSTOMER_ACTIVATED_ROUTING_KEY,
                customerActivatedEvent,
                correlationData
        );
    }

}
