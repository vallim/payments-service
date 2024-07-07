package com.vallim.payments.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vallim.payments.model.Payment;
import com.vallim.payments.service.PaymentService;
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
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
        public void testCreatePayment_returns201OnSuccess() throws Exception {

            mockMvc.perform(post("/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new Payment()))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());

            verify(paymentService).save(any());
    }

    @Test
    public void testCreatePayment_throwsBadRequestOnIllegalArgumentException() throws Exception {

        doThrow(new IllegalArgumentException()).when(paymentService).save(any());

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new Payment()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(paymentService).save(any());
    }

    @Test
    public void testCreatePayment_throws500OnUnexpectedException() throws Exception {

        doThrow(new RuntimeException()).when(paymentService).save(any());

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new Payment()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        verify(paymentService).save(any());
    }
}
