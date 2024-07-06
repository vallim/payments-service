package com.vallim.payments.api;

import com.vallim.payments.model.Webhook;
import com.vallim.payments.repository.WebhookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/webhooks")
@RestController
public class WebhooksController {

    private final WebhookRepository webhookRepository;

    public WebhooksController(WebhookRepository webhookRepository) {
        this.webhookRepository = webhookRepository;
    }

    @GetMapping
    public Iterable<Webhook> findAll() {
        return webhookRepository.findAll();
    }

    @PostMapping()
    public ResponseEntity save(@RequestBody Webhook webhook) {
        webhookRepository.save(webhook);

        return ResponseEntity.status(201).build();
    }

}
