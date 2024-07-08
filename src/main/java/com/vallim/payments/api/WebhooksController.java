package com.vallim.payments.api;

import com.vallim.payments.model.Webhook;
import com.vallim.payments.repository.WebhookRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/webhooks")
@RestController
public class WebhooksController {

    private final WebhookRepository webhookRepository;

    public WebhooksController(WebhookRepository webhookRepository) {
        this.webhookRepository = webhookRepository;
    }

    @Operation(summary = "Lists all webhooks")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Iterable<Webhook> findAll() {
        return webhookRepository.findAll();
    }

    @Operation(
            summary = "Create a new webhook",
            description = "Creates a new webhook record in the system. This webhook will be notified whenever a new payment is created."
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity save(@RequestBody Webhook webhook) {
        webhookRepository.save(webhook);

        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Removes a webhook")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable("id") Long id) {
        webhookRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
