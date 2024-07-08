package com.vallim.payments.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vallim.payments.model.OutboxEvent;
import com.vallim.payments.model.OutboxEvent.OutboxEventType;
import com.vallim.payments.model.Payment;
import com.vallim.payments.repository.OutboxEventRepository;
import com.vallim.payments.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PaymentService {

    private PaymentRepository paymentRepository;
    private CardNumberValidator cardNumberValidator;
    private OutboxEventRepository outBoxEventRepository;
    private ObjectMapper objectMapper;
    private AesCardNumberCrypto aesCardNumberCrypto;

    public PaymentService(PaymentRepository paymentRepository, CardNumberValidator cardNumberValidator,
                          OutboxEventRepository outBoxEventRepository, ObjectMapper objectMapper,
                          AesCardNumberCrypto aesCardNumberCrypto) {
        this.paymentRepository = paymentRepository;
        this.cardNumberValidator = cardNumberValidator;
        this.outBoxEventRepository = outBoxEventRepository;
        this.objectMapper = objectMapper;
        this.aesCardNumberCrypto = aesCardNumberCrypto;
    }

    @Transactional
    public void save(Payment payment) {
        if (!cardNumberValidator.isValid(payment.getCardNumber())) {
            throw new IllegalArgumentException("card number is not valid");
        }
        String encryptedCardNumber = aesCardNumberCrypto.encrypt(payment.getCardNumber());
        payment.setCardNumber(encryptedCardNumber);
        paymentRepository.save(payment);

        final OutboxEvent outboxEvent = createPaymentCreatedEvent(payment);

        outBoxEventRepository.save(outboxEvent);
    }

    private OutboxEvent createPaymentCreatedEvent(Payment payment) {
        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setType(OutboxEventType.PAYMENT_CREATED);
        outboxEvent.setPayload(serialize(payment));
        return outboxEvent;
    }

    private String serialize(Payment payment) {
        try {
            return objectMapper.writeValueAsString(payment);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Payment> findAll() {
        return StreamSupport.stream(paymentRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
