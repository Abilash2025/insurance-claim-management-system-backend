package com.icms.customerservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CUSTOMER_EXCHANGE = "customer.exchange";

    public static final String CUSTOMER_CREATED_QUEUE = "customer.created.queue";
    public static final String CUSTOMER_CREATED_ROUTING_KEY = "customer.created";

    public static final String CUSTOMER_DEACTIVATED_QUEUE = "customer.deactivated.queue";
    public static final String CUSTOMER_DEACTIVATED_ROUTING_KEY = "customer.deactivated";

    public static final String CUSTOMER_ACTIVATED_QUEUE = "customer.activated.queue";
    public static final String CUSTOMER_ACTIVATED_ROUTING_KEY = "customer.activated";

    private final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMessageConverter(jacksonJsonMessageConverter());

        rabbitTemplate.setConfirmCallback((eventName,ack,cause ) -> {
            if(ack){
                logger.info("{} published successfully", eventName);
            }
            else {
                logger.info("{} publish failed, cause: {}",eventName,cause);
            }
        });

        rabbitTemplate.setReturnsCallback(returned -> {
            logger.info(String.valueOf(returned.getMessage()));
            logger.info(String.valueOf(returned.getReplyCode()));
            logger.info(returned.getReplyText());
            logger.info(returned.getExchange());
            logger.info(returned.getRoutingKey());
        });

        return rabbitTemplate;
    }

    @Bean
    public TopicExchange customerExchange(){
        return new TopicExchange(CUSTOMER_EXCHANGE);
    }

    @Bean
    public Queue customerCreatedQueue(){
        return new Queue(CUSTOMER_CREATED_QUEUE);
    }

    @Bean
    public Binding customerCreatedBinding(){
        return BindingBuilder.bind(customerCreatedQueue())
                .to(customerExchange())
                .with(CUSTOMER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue customerDeactivatedQueue(){
        return new Queue(CUSTOMER_DEACTIVATED_QUEUE);
    }

    @Bean
    public Binding customerDeactivatedBinding(){
        return BindingBuilder.bind(customerDeactivatedQueue())
                .to(customerExchange())
                .with(CUSTOMER_DEACTIVATED_ROUTING_KEY);
    }

    @Bean
    public Queue customerActivatedQueue(){
        return new Queue(CUSTOMER_ACTIVATED_QUEUE);
    }

    @Bean
    public Binding customerActivatedBinding(){
        return BindingBuilder.bind(customerActivatedQueue())
                .to(customerExchange())
                .with(CUSTOMER_ACTIVATED_ROUTING_KEY);
    }

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter(){
        return new JacksonJsonMessageConverter();
    }
}
