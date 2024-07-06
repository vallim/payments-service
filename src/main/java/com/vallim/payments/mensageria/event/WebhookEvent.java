package com.vallim.payments.mensageria.event;

import com.vallim.payments.model.Payment;
import com.vallim.payments.model.Webhook;

import java.io.Serializable;

public class WebhookEvent implements Serializable {

    private Webhook webhook;
    private Payment payment;

    public WebhookEvent(Webhook webhook, Payment payment) {
        this.webhook = webhook;
        this.payment = payment;
    }

    public WebhookEvent() {
    }

    public Webhook getWebhook() {
        return webhook;
    }

    public void setWebhook(Webhook webhook) {
        this.webhook = webhook;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
