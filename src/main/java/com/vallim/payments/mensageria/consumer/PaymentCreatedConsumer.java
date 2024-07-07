package com.vallim.payments.mensageria.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vallim.payments.repository.WebhookRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentCreatedConsumer {

    private final WebhookRepository webhookRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public PaymentCreatedConsumer(WebhookRepository webhookRepository,
                                  ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.webhookRepository = webhookRepository;
        this.objectMapper = objectMapper;
    }

    public void process(String event) {

        webhookRepository.findAll().forEach(webhook -> {
            restTemplate.postForEntity(webhook.getCallbackUrl(), event, String.class);
        });
    }
}
