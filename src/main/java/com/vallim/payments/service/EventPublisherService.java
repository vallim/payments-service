package com.vallim.payments.service;

import com.vallim.payments.model.OutboxEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventPublisherService {

    private final RabbitTemplate rabbitTemplate;

    public EventPublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEvent(OutboxEvent event) {
        String exchange = determineExchange(event.getType());
        String routingKey = determineRoutingKey(event.getType());
        rabbitTemplate.convertAndSend(exchange, routingKey, event.getPayload());
    }

    private String determineExchange(OutboxEvent.OutboxEventType eventType) {
        switch (eventType) {
            case PaymentCreated:
                return "payment";
            default:
                return "default";
        }
    }

    private String determineRoutingKey(OutboxEvent.OutboxEventType eventType) {
        switch (eventType) {
            case PaymentCreated:
                return "payment.created";
            default:
                return "default.routing";
        }
    }
}
