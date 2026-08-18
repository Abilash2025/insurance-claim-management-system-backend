package com.icms.policyservice.config;

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

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);

    public static final String POLICY_EXCHANGE = "policy.exchange";

    public static final String POLICY_CREATED_QUEUE = "policy.created.queue";
    public static final String POLICY_CREATED_ROUTING_KEY = "policy.created";

    public static final String POLICY_UPDATED_QUEUE = "policy.updated.queue";
    public static final String POLICY_UPDATED_ROUTING_KEY = "policy.updated";

    public static final String POLICY_DEACTIVATED_QUEUE = "policy.deactivated.queue";
    public static final String POLICY_DEACTIVATED_ROUTING_KEY = "policy.deactivated";

    public static final String POLICY_ACTIVATED_QUEUE = "policy.activated.queue";
    public static final String POLICY_ACTIVATED_ROUTING_KEY = "policy.activated";


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        rabbitTemplate.setConfirmCallback((eventName, ack, cause) -> {
            if(ack){
                logger.info("{} published successfully",eventName);
            } else {
                logger.info("Failed to publish PolicyCreatedEvent. Cause: {}", cause);
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
    public TopicExchange policyExchange(){
        return new TopicExchange(POLICY_EXCHANGE);
    }

    @Bean
    public Queue policyCreatedQueue(){
        return new Queue(POLICY_CREATED_QUEUE);
    }

    @Bean
    public Binding policyCreatedBinding(){
        return BindingBuilder
                .bind(policyCreatedQueue())
                .to(policyExchange())
                .with(POLICY_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue policyUpdatedQueue(){
        return new Queue(POLICY_UPDATED_QUEUE);
    }

    @Bean
    public Binding policyUpdatedBinding(){
        return BindingBuilder.bind(policyUpdatedQueue())
                .to(policyExchange())
                .with(POLICY_UPDATED_ROUTING_KEY);
    }

    @Bean
    public Queue policyDeactivatedQueue(){
        return new Queue(POLICY_DEACTIVATED_QUEUE);
    }

    @Bean
    public Binding policyDeactivatedBinding() {
        return BindingBuilder.bind(policyDeactivatedQueue())
                .to(policyExchange())
                .with(POLICY_DEACTIVATED_ROUTING_KEY);
    }

    @Bean
    public Queue policyActivatedQueue(){
        return new Queue(POLICY_ACTIVATED_QUEUE);
    }

    @Bean
    public Binding policyActivatedBinding() {
        return BindingBuilder.bind(policyActivatedQueue())
                .to(policyExchange())
                .with(POLICY_ACTIVATED_ROUTING_KEY);
    }
    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter(){
        return new JacksonJsonMessageConverter();
    }
}
