package com.vallim.payments.mensageria.consumer;

import com.vallim.payments.model.Webhook;
import com.vallim.payments.repository.WebhookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentCreatedConsumerTest {

    @Mock
    private WebhookRepository webhookRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PaymentCreatedConsumer paymentCreatedConsumer;

    private Webhook webhook;
    private String event;

    @BeforeEach
    void setUp() {
        webhook = new Webhook();
        webhook.setCallbackUrl("http://example.com/webhook");
        event = "{\"event\":\"payment_created\",\"data\":\"sample data\"}";
    }

    @Test
    void shouldProcessEventAndNotifyWebhooks() {
        when(webhookRepository.findAll()).thenReturn(Collections.singletonList(webhook));

        paymentCreatedConsumer.process(event);

        verify(restTemplate).postForEntity(webhook.getCallbackUrl(), event, String.class);
    }

    @Test
    void shouldThrowExceptionWhenWebhookNotificationFails() {
        when(webhookRepository.findAll()).thenReturn(Collections.singletonList(webhook));
        doThrow(new RestClientException("Failed to notify")).when(restTemplate)
                .postForEntity(webhook.getCallbackUrl(), event, String.class);

        assertThrows(RestClientException.class, () -> paymentCreatedConsumer.process(event));

        verify(restTemplate).postForEntity(webhook.getCallbackUrl(), event, String.class);
    }

    @Test
    void shouldProcessEventWithoutWebhooks() {
        when(webhookRepository.findAll()).thenReturn(Collections.emptyList());

        paymentCreatedConsumer.process(event);

        verify(restTemplate, never()).postForEntity(anyString(), anyString(), eq(String.class));
    }
}
