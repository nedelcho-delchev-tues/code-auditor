package com.code.auditor.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "student_submission_exchange";
    public static final String QUEUE_NAME = "student_submission_queue";

    @Value("${spring.rabbitmq.host}")
    private String rabbitMqHost;

    @Value("${spring.rabbitmq.port}")
    private int rabbitMqPort;

    @Value("${spring.rabbitmq.username}")
    private String rabbitMqUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitMqPassword;

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitMqHost);
        connectionFactory.setPort(rabbitMqPort);
        connectionFactory.setUsername(rabbitMqUsername);
        connectionFactory.setPassword(rabbitMqPassword);

        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(EXCHANGE_NAME);
        rabbitTemplate.setDefaultReceiveQueue(QUEUE_NAME);

        return rabbitTemplate;
    }

    @Bean
    public Exchange studentSubmissionExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue studentSubmissionQueue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Binding binding(Exchange recordInsertedExchange, Queue recordInsertedQueue) {
        return BindingBuilder.bind(recordInsertedQueue).to(recordInsertedExchange).with("submission.saved").noargs();
    }
}
