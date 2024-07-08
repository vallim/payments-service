package com.vallim.payments.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vallim.payments.infra.GlobalExceptionHandler;
import com.vallim.payments.model.Payment;
import com.vallim.payments.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PaymentsControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentsController controller;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(GlobalExceptionHandler.class).build();

        objectMapper = new ObjectMapper();
    }

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

    @Test
    public void testListPayments_returns200OnSuccess() throws Exception {

        when(paymentService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new Payment()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        verify(paymentService).findAll();
    }
}
