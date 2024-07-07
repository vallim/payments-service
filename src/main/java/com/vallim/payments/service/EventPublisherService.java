package com.vallim.payments.service;

import com.vallim.payments.model.OutboxEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EventPublisherService {

    private static final Logger logger = LoggerFactory.getLogger(EventPublisherService.class);

    private final RabbitTemplate rabbitTemplate;

    private static final Map<OutboxEvent.OutboxEventType, String> EVENT_TYPE_TO_EXCHANGE;
    private static final Map<OutboxEvent.OutboxEventType, String> EVENT_TYPE_TO_ROUTING_KEY;

    static {
        EVENT_TYPE_TO_EXCHANGE = new HashMap<>();
        EVENT_TYPE_TO_EXCHANGE.put(OutboxEvent.OutboxEventType.PAYMENT_CREATED, "payment");

        EVENT_TYPE_TO_ROUTING_KEY = new HashMap<>();
        EVENT_TYPE_TO_ROUTING_KEY.put(OutboxEvent.OutboxEventType.PAYMENT_CREATED, "payment.created");
    }

    public EventPublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEvent(OutboxEvent event) {
        String exchange = getExchange(event.getType());
        String routingKey = getRoutingKey(event.getType());

        logger.info("Publishing event {} to topic {} and routing key {}", event.getType(), exchange, routingKey);
        rabbitTemplate.convertAndSend(exchange, routingKey, event.getPayload());
    }

    private String getExchange(OutboxEvent.OutboxEventType eventType) {
        return EVENT_TYPE_TO_EXCHANGE.getOrDefault(eventType, "default");
    }

    private String getRoutingKey(OutboxEvent.OutboxEventType eventType) {
        return EVENT_TYPE_TO_ROUTING_KEY.getOrDefault(eventType, "default.routing");
    }
}
