package com.vallim.payments.repository;

import com.vallim.payments.model.Webhook;
import org.springframework.data.repository.CrudRepository;

public interface WebhookRepository extends CrudRepository<Webhook, Long> {
}
