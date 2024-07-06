package com.vallim.payments.mensageria.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vallim.payments.mensageria.event.WebhookEvent;
import com.vallim.payments.model.OutboxEvent;
import com.vallim.payments.model.OutboxEvent.OutboxEventType;
import com.vallim.payments.model.Payment;
import com.vallim.payments.model.Webhook;
import com.vallim.payments.repository.OutboxEventRepository;
import com.vallim.payments.repository.WebhookRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PaymentCreatedConsumer {

    private final OutboxEventRepository outBoxEventRepository;
    private final WebhookRepository webhookRepository;
    private final ObjectMapper objectMapper;

    public PaymentCreatedConsumer(OutboxEventRepository outBoxEventRepository, WebhookRepository webhookRepository,
                                  ObjectMapper objectMapper) {
        this.outBoxEventRepository = outBoxEventRepository;
        this.webhookRepository = webhookRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void process(Payment payment) {
        webhookRepository.findAll().forEach(webhook -> {
            final OutboxEvent outboxEvent = new OutboxEvent();
            outboxEvent.setType(OutboxEventType.WebhookNotificationCreated);
            WebhookEvent webhookEvent = new WebhookEvent(webhook, payment);
            outboxEvent.setPayload(serialize(webhookEvent));

            outBoxEventRepository.save(outboxEvent);
        });
    }

    private String serialize(WebhookEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
