package com.vallim.payments.api;

import com.vallim.payments.model.Payment;
import com.vallim.payments.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/payments")
@RestController
public class PaymentsController {

    private final PaymentService paymentService;

    public PaymentsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity save(@RequestBody Payment payment) {
        paymentService.save(payment);

        return ResponseEntity.status(201).build();
    }

    @GetMapping
    public List<Payment> list() {
        return paymentService.findAll();
    }
}
