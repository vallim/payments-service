package com.vallim.payments.mensageria.consumer;

import com.vallim.payments.repository.WebhookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentCreatedConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PaymentCreatedConsumer.class);

    private final WebhookRepository webhookRepository;
    private final RestTemplate restTemplate;

    public PaymentCreatedConsumer(WebhookRepository webhookRepository, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.webhookRepository = webhookRepository;
    }

    public void process(String event) {
        logger.info("Processing PaymentCreatedEvent: {}", event);

        webhookRepository.findAll().forEach(webhook -> {
            logger.info("Notifying webhook {} with payload {}", webhook.getCallbackUrl(), event);

            try {
                restTemplate.postForEntity(webhook.getCallbackUrl(), event, String.class);
            } catch (RestClientException ex) {
                logger.error("Failed while trying to notify webhook", ex);
                throw ex;
            }
        });
    }
}
