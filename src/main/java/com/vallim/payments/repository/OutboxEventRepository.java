package com.vallim.payments.repository;

import com.vallim.payments.model.OutboxEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    @Query("select oe from OutboxEvent oe where oe.status = 'PENDING' order by oe.createdAt")
    Page<OutboxEvent> findPendingEvents(Pageable pageable);
}
