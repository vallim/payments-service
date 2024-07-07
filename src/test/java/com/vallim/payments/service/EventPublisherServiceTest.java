package com.vallim.payments.service;

import com.vallim.payments.model.OutboxEvent;
import com.vallim.payments.model.OutboxEvent.OutboxEventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@ExtendWith(MockitoExtension.class)
public class EventPublisherServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private EventPublisherService eventPublisherService;

    @Test
    public void testPublishEvent_paymentCreated_sendsToCorrectExchangeAndRoutingKey() {
        OutboxEvent event = new OutboxEvent();
        event.setType(OutboxEventType.PAYMENT_CREATED);
        event.setPayload("payment data");

        eventPublisherService.publishEvent(event);

        Mockito.verify(rabbitTemplate).convertAndSend("payment", "payment.created", "payment data");
    }

    @Test
    public void testPublishEvent_unknownEventType_sendsToDefaultExchangeAndRoutingKey() {
        OutboxEvent event = new OutboxEvent();
        event.setType(OutboxEventType.UNKNOWN);
        event.setPayload("some data");

        eventPublisherService.publishEvent(event);

        Mockito.verify(rabbitTemplate).convertAndSend("default", "default.routing", "some data");
    }
}
