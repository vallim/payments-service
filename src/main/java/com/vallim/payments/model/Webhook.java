package com.vallim.payments.model;

import jakarta.persistence.*;

@Entity
public class Webhook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "payment_id_seq", sequenceName = "payment_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "callback_url")
    private String callbackUrl;

    public Long getId() {
        return id;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
