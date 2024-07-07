package com.vallim.payments.infra;

import com.vallim.payments.mensageria.consumer.PaymentCreatedConsumer;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;

@Configuration
public class QueueConfig {

    private static final String TOPIC_NAME = "payment";
    private static final String QUEUE_NAME = "payment-created";
    private static final String RETRY_QUEUE_NAME = "payment-created-retry";
    private static final String DLQ_QUEUE_NAME = "payment-created-dlq";
    private static final String MAIN_ROUTING_KEY = "payment.created";

    @Value("${spring.rabbitmq.listener.retry.max-attempts:3}")
    private int maxAttempts;

    @Bean
    public Queue paymentCreatedQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .deadLetterExchange(TOPIC_NAME)
                .deadLetterRoutingKey(RETRY_QUEUE_NAME)
                .build();
    }

    @Bean
    public Queue paymentCreatedRetryQueue() {
        return QueueBuilder.durable(RETRY_QUEUE_NAME)
                .deadLetterExchange(TOPIC_NAME)
                .deadLetterRoutingKey(MAIN_ROUTING_KEY)
                .ttl(10000) // 10 seconds TTL
                .build();
    }

    @Bean
    public Queue paymentCreatedDLQ() {
        return QueueBuilder.durable(DLQ_QUEUE_NAME).build();
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(TOPIC_NAME);
    }

    @Bean
    public Binding paymentCreatedBinding(Queue paymentCreatedQueue, TopicExchange exchange) {
        return BindingBuilder.bind(paymentCreatedQueue).to(exchange).with(MAIN_ROUTING_KEY);
    }

    @Bean
    public Binding paymentCreatedRetryBinding(Queue paymentCreatedRetryQueue, TopicExchange exchange) {
        return BindingBuilder.bind(paymentCreatedRetryQueue).to(exchange).with(RETRY_QUEUE_NAME);
    }

    @Bean
    public Binding paymentCreatedDLQBinding(Queue paymentCreatedDLQ, TopicExchange exchange) {
        return BindingBuilder.bind(paymentCreatedDLQ).to(exchange).with(DLQ_QUEUE_NAME);
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        container.setAdviceChain(retryInterceptor());

        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(PaymentCreatedConsumer paymentCreatedConsumer) {
        MessageListenerAdapter messageAdapter = new MessageListenerAdapter(paymentCreatedConsumer, "process");
        messageAdapter.setMessageConverter(messageConverter());

        return messageAdapter;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public StatefulRetryOperationsInterceptor retryInterceptor() {
        return RetryInterceptorBuilder.stateful()
                .maxAttempts(maxAttempts)
                .build();
    }
}
