package com.icms.notificationservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CUSTOMER_EXCHANGE = "customer.exchange";

    public static final String CUSTOMER_CREATED_QUEUE = "customer.created.notification.queue";
    public static final String CUSTOMER_ACTIVATED_QUEUE = "customer.activated.notification.queue";
    public static final String CUSTOMER_DEACTIVATED_QUEUE = "customer.deactivated.notification.queue";

    public static final String CUSTOMER_CREATED_ROUTING_KEY = "customer.created";
    public static final String CUSTOMER_ACTIVATED_ROUTING_KEY = "customer.activated";
    public static final String CUSTOMER_DEACTIVATED_ROUTING_KEY = "customer.deactivated";


    public static final String POLICY_EXCHANGE = "policy.exchange";

    public static final String POLICY_CREATED_QUEUE = "policy.created.notification.queue";
    public static final String POLICY_UPDATED_QUEUE = "policy.updated.notification.queue";
    public static final String POLICY_ACTIVATED_QUEUE = "policy.activated.notification.queue";
    public static final String POLICY_DEACTIVATED_QUEUE = "policy.deactivated.notification.queue";

    public static final String POLICY_CREATED_ROUTING_KEY = "policy.created";
    public static final String POLICY_UPDATED_ROUTING_KEY = "policy.updated";
    public static final String POLICY_ACTIVATED_ROUTING_KEY = "policy.activated";
    public static final String POLICY_DEACTIVATED_ROUTING_KEY = "policy.deactivated";

    public static final String CLAIM_EXCHANGE = "claim.exchange";

    public static final String CLAIM_CREATED_QUEUE = "claim.created.notification.queue";
    public static final String CLAIM_APPROVED_QUEUE = "claim.approved.notification.queue";
    public static final String CLAIM_REJECTED_QUEUE = "claim.rejected.notification.queue";

    public static final String CLAIM_CREATED_ROUTING_KEY = "claim.created";
    public static final String CLAIM_APPROVED_ROUTING_KEY = "claim.approved";
    public static final String CLAIM_REJECTED_ROUTING_KEY = "claim.rejected";

    public static final String EMAIL_EXCHANGE = "email.exchange";
    public static final String EMAIL_QUEUE = "email.queue";
    public static final String EMAIL_ROUTING_KEY = "notification.email";

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);


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
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonJsonMessageConverter());

        return factory;
    }


    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public TopicExchange customerExchange() {
        return new TopicExchange(CUSTOMER_EXCHANGE);
    }

    @Bean
    public Queue customerCreatedQueue() {
        return new Queue(CUSTOMER_CREATED_QUEUE);
    }

    @Bean
    public Queue customerActivatedQueue() {
        return new Queue(CUSTOMER_ACTIVATED_QUEUE);
    }

    @Bean
    public Queue customerDeactivatedQueue() {
        return new Queue(CUSTOMER_DEACTIVATED_QUEUE);
    }

    @Bean
    public Binding customerCreatedBinding() {
        return BindingBuilder.bind(customerCreatedQueue())
                .to(customerExchange())
                .with(CUSTOMER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding customerActivatedBinding() {
        return BindingBuilder.bind(customerActivatedQueue())
                .to(customerExchange())
                .with(CUSTOMER_ACTIVATED_ROUTING_KEY);
    }

    @Bean
    public Binding customerDeactivatedBinding() {
        return BindingBuilder.bind(customerDeactivatedQueue())
                .to(customerExchange())
                .with(CUSTOMER_DEACTIVATED_ROUTING_KEY);
    }

    @Bean
    public TopicExchange policyExchange() {
        return new TopicExchange(POLICY_EXCHANGE);
    }

    @Bean
    public Queue policyCreatedQueue() {
        return new Queue(POLICY_CREATED_QUEUE);
    }

    @Bean
    public Queue policyUpdatedQueue() {
        return new Queue(POLICY_UPDATED_QUEUE);
    }

    @Bean
    public Queue policyActivatedQueue() {
        return new Queue(POLICY_ACTIVATED_QUEUE);
    }

    @Bean
    public Queue policyDeactivatedQueue() {
        return new Queue(POLICY_DEACTIVATED_QUEUE);
    }

    @Bean
    public Binding policyCreatedBinding() {
        return BindingBuilder.bind(policyCreatedQueue())
                .to(policyExchange())
                .with(POLICY_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding policyUpdatedBinding() {
        return BindingBuilder.bind(policyUpdatedQueue())
                .to(policyExchange())
                .with(POLICY_UPDATED_ROUTING_KEY);
    }

    @Bean
    public Binding policyActivatedBinding() {
        return BindingBuilder.bind(policyActivatedQueue())
                .to(policyExchange())
                .with(POLICY_ACTIVATED_ROUTING_KEY);
    }

    @Bean
    public Binding policyDeactivatedBinding() {
        return BindingBuilder.bind(policyDeactivatedQueue())
                .to(policyExchange())
                .with(POLICY_DEACTIVATED_ROUTING_KEY);
    }

    @Bean
    public TopicExchange claimExchange() {
        return new TopicExchange(CLAIM_EXCHANGE);
    }

    @Bean
    public Queue claimCreatedQueue() {
        return new Queue(CLAIM_CREATED_QUEUE);
    }

    @Bean
    public Queue claimApprovedQueue() {
        return new Queue(CLAIM_APPROVED_QUEUE);
    }

    @Bean
    public Queue claimRejectedQueue() {
        return new Queue(CLAIM_REJECTED_QUEUE);
    }

    @Bean
    public Binding claimCreatedBinding() {
        return BindingBuilder.bind(claimCreatedQueue())
                .to(claimExchange())
                .with(CLAIM_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding claimApprovedBinding() {
        return BindingBuilder.bind(claimApprovedQueue())
                .to(claimExchange())
                .with(CLAIM_APPROVED_ROUTING_KEY);
    }

    @Bean
    public Binding claimRejectedBinding() {
        return BindingBuilder.bind(claimRejectedQueue())
                .to(claimExchange())
                .with(CLAIM_REJECTED_ROUTING_KEY);
    }

    @Bean
    public TopicExchange emailExchange(){
        return new TopicExchange(EMAIL_EXCHANGE);
    }

    @Bean
    public Queue emailQueue(){
        return new Queue(EMAIL_QUEUE);
    }

    @Bean
    public Binding emailBinding(){
        return BindingBuilder.bind(emailQueue())
                .to(emailExchange())
                .with(EMAIL_ROUTING_KEY);
    }
}
