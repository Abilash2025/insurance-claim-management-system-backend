package com.icms.emailservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EMAIL_EXCHANGE = "email.exchange";
    public static final String EMAIL_QUEUE = "email.queue";
    public static final String EMAIL_ROUTING_KEY = "notification.email";

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory){

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonJsonMessageConverter());

        return factory;

    }

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter(){
        return new JacksonJsonMessageConverter();
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
