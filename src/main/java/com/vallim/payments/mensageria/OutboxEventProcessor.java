package com.vallim.payments.mensageria;

import com.vallim.payments.model.OutboxEvent;
import com.vallim.payments.repository.OutboxEventRepository;
import com.vallim.payments.service.EventPublisherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OutboxEventProcessor {

    private static final int FIRST_PAGE = 0;
    private static final int PAGE_SIZE = 100;
    private static final int ONE_MINUTE = 1000;

    private final OutboxEventRepository repository;
    private final EventPublisherService eventPublisherService;

    public OutboxEventProcessor(OutboxEventRepository repository, EventPublisherService eventPublisherService) {
        this.repository = repository;
        this.eventPublisherService = eventPublisherService;
    }

    @Scheduled(fixedRate = ONE_MINUTE)
    public void processOutboxEvents() {
        PageRequest firstPageRequest = createFirstPage();
        Page<OutboxEvent> currentPage = repository.findPendingEvents(firstPageRequest);

        do {
            currentPage.getContent()
                    .forEach(this::processEvent);

            currentPage = repository.findPendingEvents(currentPage.nextPageable());
        } while (currentPage.hasNext());
    }

    private void processEvent(OutboxEvent event) {
        event.markAsProcessing();
        repository.save(event);

        eventPublisherService.publishEvent(event);

        event.markAsProcessed();
        repository.save(event);
    }

    PageRequest createFirstPage() {
        return PageRequest.of(FIRST_PAGE, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "createdAt"));
    }
}
