package com.vallim.payments.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vallim.payments.model.OutboxEvent;
import com.vallim.payments.model.Payment;
import com.vallim.payments.repository.OutboxEventRepository;
import com.vallim.payments.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CardNumberValidator cardNumberValidator;

    @Mock
    private OutboxEventRepository outBoxEventRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AesCardNumberCrypto aesCardNumberCrypto;

    @InjectMocks
    private PaymentService paymentService;

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        payment.setCardNumber("1234567812345678");
    }

    @Test
    void shouldSavePaymentAndOutboxEventWhenCardNumberIsValid() throws JsonProcessingException {
        when(cardNumberValidator.isValid(payment.getCardNumber())).thenReturn(true);
        when(objectMapper.writeValueAsString(any(Payment.class)))
                .thenReturn("{\"cardNumber\":\"1234567812345678\",\"amount\":100.0}");

        paymentService.save(payment);

        verify(aesCardNumberCrypto).encrypt("1234567812345678");
        verify(paymentRepository).save(payment);
        verify(outBoxEventRepository).save(any(OutboxEvent.class));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCardNumberIsInvalid() {
        when(cardNumberValidator.isValid(payment.getCardNumber())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> paymentService.save(payment));

        verify(aesCardNumberCrypto, never()).encrypt(any());
        verify(paymentRepository, never()).save(any(Payment.class));
        verify(outBoxEventRepository, never()).save(any(OutboxEvent.class));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenSerializationFails() throws JsonProcessingException {
        when(cardNumberValidator.isValid(payment.getCardNumber())).thenReturn(true);
        when(objectMapper.writeValueAsString(any(Payment.class)))
                .thenThrow(new JsonProcessingException("Serialization error") {});

        assertThrows(RuntimeException.class, () -> paymentService.save(payment));

        verify(aesCardNumberCrypto).encrypt("1234567812345678");
        verify(paymentRepository).save(payment);
        verify(outBoxEventRepository, never()).save(any(OutboxEvent.class));
    }

    @Test
    void shouldHandleDatabaseExceptions() {
        when(cardNumberValidator.isValid(payment.getCardNumber())).thenReturn(true);
        doThrow(new InvalidDataAccessApiUsageException("Database error"))
                .when(paymentRepository).save(any(Payment.class));

        assertThrows(DataAccessException.class, () -> paymentService.save(payment));

        verify(paymentRepository).save(payment);
        verify(outBoxEventRepository, never()).save(any(OutboxEvent.class));
    }
}
