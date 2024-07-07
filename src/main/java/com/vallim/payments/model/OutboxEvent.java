package com.vallim.payments.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@EntityListeners(AuditingEntityListener.class)
@Table(name = "outbox_event")
@Entity
public class OutboxEvent {

    public enum OutboxEventType {
        PAYMENT_CREATED,
        UNKNOWN
    }

    enum OutboxEventStatus {
        PENDING,
        PROCESSING,
        FAILED,
        PROCESSED
    }

    @Id
    @GeneratedValue(generator = "outbox_event_id_seq")
    @GenericGenerator(name = "outbox_event_id_seq", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private OutboxEventType type;

    @ColumnTransformer(write = "?::json")
    private String payload;

    @Enumerated(EnumType.STRING)
    private OutboxEventStatus status = OutboxEventStatus.PENDING;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void setType(OutboxEventType type) {
        this.type = type;
    }

    public OutboxEventType getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void markAsProcessed() {
        this.status = OutboxEventStatus.PROCESSED;
    }

    public void markAsProcessing() {
        this.status = OutboxEventStatus.PROCESSING;
    }
}
