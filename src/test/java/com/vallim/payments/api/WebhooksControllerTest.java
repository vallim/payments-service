package com.vallim.payments.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vallim.payments.infra.GlobalExceptionHandler;
import com.vallim.payments.model.Webhook;
import com.vallim.payments.repository.WebhookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class WebhooksControllerTest {

    @Mock
    private WebhookRepository webhookRepository;

    @InjectMocks
    private WebhooksController controller;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(GlobalExceptionHandler.class).build();
    }

    @Test
        public void testCreateWebhook_returns201OnSuccess() throws Exception {

            mockMvc.perform(post("/webhooks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new Webhook()))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    public void testCreateWebhook_throws500OnUnexpectedException() throws Exception {

        when(webhookRepository.save(any())).thenThrow(new RuntimeException());

        mockMvc.perform(post("/webhooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new Webhook()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        verify(webhookRepository).save(any());
    }
}
