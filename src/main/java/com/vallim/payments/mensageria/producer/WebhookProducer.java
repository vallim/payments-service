package com.vallim.payments.mensageria.producer;

import com.vallim.payments.mensageria.event.WebhookEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebhookProducer {

    private final RabbitTemplate rabbitTemplate;

    public WebhookProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(WebhookEvent event) {
        rabbitTemplate.convertAndSend("webhook-exchange", "com.vallim.payments.webhook", event);
    }
}
