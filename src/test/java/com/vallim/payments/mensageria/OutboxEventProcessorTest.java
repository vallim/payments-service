package com.vallim.payments.mensageria;

import com.vallim.payments.model.OutboxEvent;
import com.vallim.payments.repository.OutboxEventRepository;
import com.vallim.payments.service.EventPublisherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OutboxEventProcessorTest {

    @Mock
    private OutboxEventRepository repository;

    @Mock
    private EventPublisherService eventPublisherService;

    @InjectMocks
    private OutboxEventProcessor outboxEventProcessor;


    @Test
    public void testProcessOutboxEvents() {
        OutboxEvent event = new OutboxEvent();
        Page<OutboxEvent> page = new PageImpl<>(List.of(event));

        when(repository.findPendingEvents(any(Pageable.class))).thenReturn(page).thenReturn(Page.empty());

        outboxEventProcessor.processOutboxEvents();

        verify(repository, times(2)).findPendingEvents(any(Pageable.class));
        verify(repository, times(2)).save(any(OutboxEvent.class));
        verify(eventPublisherService, times(1)).publishEvent(any(OutboxEvent.class));
    }

    @Test
    public void testCreateFirstPage() {
        PageRequest pageRequest = outboxEventProcessor.createFirstPage();
        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(100, pageRequest.getPageSize());
        assertEquals(Sort.by(Sort.Direction.ASC, "createdAt"), pageRequest.getSort());
    }
}
