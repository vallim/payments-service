package com.vallim.payments.api;

import com.vallim.payments.model.Payment;
import com.vallim.payments.service.PaymentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/payments")
@RestController
public class PaymentsController {

    private final PaymentsService paymentsService;

    public PaymentsController(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;
    }

    @PostMapping()
    public ResponseEntity save(@RequestBody Payment payment) {
        paymentsService.save(payment);

        return ResponseEntity.status(201).build();
    }
}
