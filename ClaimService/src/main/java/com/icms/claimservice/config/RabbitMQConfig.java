package com.icms.claimservice.config;

import org.apache.commons.codec.BinaryDecoder;
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

    public static final String CLAIM_EXCHANGE = "claim.exchange";

    public static final String CLAIM_CREATED_QUEUE = "claim.created.queue";
    public static final String CLAIM_CREATED_ROUTING_KEY = "claim.created";

    public static final String CLAIM_APPROVED_QUEUE = "claim.approved.queue";
    public static final String CLAIM_APPROVED_ROUTING_KEY = "claim.approved";

    public static final String CLAIM_REJECTED_QUEUE = "claim.rejected.queue";
    public static final String CLAIM_REJECTED_ROUTING_KEY = "claim.rejected";

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        rabbitTemplate.setConfirmCallback((eventName,ack,cause) -> {
            if(ack){
                logger.info("{} successfully published.", eventName);
            } else {
                logger.info("{} publishing failed. cause: {}", eventName,cause);
            }
        });

        rabbitTemplate.setReturnsCallback(returned -> {
            logger.info(String.valueOf(returned.getReplyCode()));
            logger.info(String.valueOf(returned.getMessage()));
            logger.info(returned.getExchange());
            logger.info(returned.getRoutingKey());
            logger.info(returned.getReplyText());
        });
        return rabbitTemplate;
    }

    @Bean
    public TopicExchange claimExchange(){return new TopicExchange(CLAIM_EXCHANGE);}

    @Bean
    public Queue claimCreatedQueue(){return new Queue(CLAIM_CREATED_QUEUE);}

    @Bean
    public Binding claimCreatedBinding(){
        return BindingBuilder.bind(claimCreatedQueue())
                .to(claimExchange())
                .with(CLAIM_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue claimApprovedQueue(){return new Queue(CLAIM_APPROVED_QUEUE);}

    @Bean
    public Binding claimApprovedBinding(){
        return BindingBuilder.bind(claimApprovedQueue())
                .to(claimExchange())
                .with(CLAIM_APPROVED_ROUTING_KEY);
    }

    @Bean
    public Queue claimRejectedQueue(){return new Queue(CLAIM_REJECTED_QUEUE);}

    @Bean Binding claimRejectedBinding(){
        return BindingBuilder.bind(claimRejectedQueue())
                .to(claimExchange())
                .with(CLAIM_REJECTED_ROUTING_KEY);
    }

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter(){
        return new JacksonJsonMessageConverter();
    }
}
