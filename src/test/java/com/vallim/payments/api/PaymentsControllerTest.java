package com.vallim.payments.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vallim.payments.model.Payment;
import com.vallim.payments.service.PaymentsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentsService paymentsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
        public void testCreatePayment_returns201OnSuccess() throws Exception {

            mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new Payment()))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());

            verify(paymentsService).save(any());
    }

    @Test
    public void testCreatePayment_throwsBadRequestOnIllegalArgumentException() throws Exception {

        doThrow(new IllegalArgumentException()).when(paymentsService).save(any());

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new Payment()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(paymentsService).save(any());
    }

    @Test
    public void testCreatePayment_throws500OnUnexpectedException() throws Exception {

        doThrow(new RuntimeException()).when(paymentsService).save(any());

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new Payment()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        verify(paymentsService).save(any());
    }
}
