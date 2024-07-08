package com.vallim.payments.api;

import com.vallim.payments.model.Payment;
import com.vallim.payments.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/payments")
@RestController
public class PaymentsController {

    private final PaymentService paymentService;

    public PaymentsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(
            summary = "Create a new payment",
            description = "Creates a payment record in the system and notifies all existing webhooks with the payment content."
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity save(@RequestBody Payment payment) {
        paymentService.save(payment);

        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Lists all payments")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Payment> list() {
        return paymentService.findAll();
    }
}
