package com.vallim.payments.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vallim.payments.model.Webhook;
import com.vallim.payments.repository.WebhookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WebhooksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebhookRepository webhookRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
