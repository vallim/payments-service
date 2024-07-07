package com.vallim.payments.infra;

import com.vallim.payments.mensageria.consumer.PaymentCreatedConsumer;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    private static final String TOPIC_NAME = "payment";
    private static final String QUEUE_NAME = "payment-created";
    private static final String RETRY_QUEUE_NAME = "payment-created-retry";
    private static final String DLQ_NAME = "payment-created-dlq";

    @Bean
    public Queue paymentCreatedQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", TOPIC_NAME)
                .withArgument("x-dead-letter-routing-key", RETRY_QUEUE_NAME)
                .build();
    }

    @Bean
    public Queue paymentCreatedRetryQueue() {
        return QueueBuilder.durable(RETRY_QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", TOPIC_NAME)
                .withArgument("x-dead-letter-routing-key", QUEUE_NAME)
                .withArgument("x-message-ttl", 60000) // 60 seconds TTL
                .build();
    }

    @Bean
    public Queue paymentCreatedDLQ() {
        return QueueBuilder.durable(DLQ_NAME).build();
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(TOPIC_NAME);
    }

    @Bean
    public Binding paymentCreatedBinding(Queue paymentCreatedQueue, TopicExchange exchange) {
        return BindingBuilder.bind(paymentCreatedQueue).to(exchange).with("payment.created");
    }

    @Bean
    public Binding paymentCreatedRetryBinding(Queue paymentCreatedRetryQueue, TopicExchange exchange) {
        return BindingBuilder.bind(paymentCreatedRetryQueue).to(exchange).with(RETRY_QUEUE_NAME);
    }

    @Bean
    public Binding paymentCreatedDLQBinding(Queue paymentCreatedDLQ, TopicExchange exchange) {
        return BindingBuilder.bind(paymentCreatedDLQ).to(exchange).with(DLQ_NAME);
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);

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
}
