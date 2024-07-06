package com.vallim.payments.mensageria.consumer;

import com.vallim.payments.mensageria.event.WebhookEvent;
import com.vallim.payments.model.Payment;
import com.vallim.payments.model.Webhook;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WebhookNotificationCreatedConsumer {

    private final RestTemplate restTemplate;

    public WebhookNotificationCreatedConsumer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void process(WebhookEvent webhookEvent) {
        Webhook webhook = webhookEvent.getWebhook();
        Payment payment = webhookEvent.getPayment();

        restTemplate.postForEntity(webhook.getCallbackUrl(), payment, String.class);
    }
}
