package com.vallim.payments.mensageria;

import com.vallim.payments.model.OutboxEvent;
import com.vallim.payments.repository.OutboxEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OutboxEventProcessor {

    //TODO this configs can be in a properties file
    private static final int FIRST_PAGE = 0;
    private static final int PAGE_SIZE = 100;
    private static final int ONE_MINUTE = 1000;

    private OutboxEventRepository repository;

    public OutboxEventProcessor(OutboxEventRepository repository) {
        this.repository = repository;
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
        event.markAsProcessed();
        repository.save(event);
    }

    private PageRequest createFirstPage() {
        return PageRequest.of(FIRST_PAGE, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "createdAt"));
    }
}
