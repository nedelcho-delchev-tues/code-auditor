package com.code.auditor.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    private static final String CODE_AUDITOR_EXCHANGE = "code_auditor_exchange";

    private static final String SUBMISSION_QUEUE = "student_submission_queue";
    private static final String SUBMISSION_SAVED_KEY = "submission.saved";

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
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public DirectExchange codeAuditorExchange() {
        return new DirectExchange(CODE_AUDITOR_EXCHANGE);
    }

    @Bean
    public Queue studentSubmissionQueue() {
        return new Queue(SUBMISSION_QUEUE);
    }

    @Bean
    public Binding submissionBinding(Queue studentSubmissionQueue, DirectExchange exchange) {
        return BindingBuilder.bind(studentSubmissionQueue).to(exchange).with(SUBMISSION_SAVED_KEY);
    }
}
