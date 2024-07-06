package com.vallim.payments.infra;

import com.vallim.payments.mensageria.OutboxEventProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private OutboxEventProcessor outboxEventProcessor;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // Application is ready, start the scheduled task
        outboxEventProcessor.processOutboxEvents();
    }
}
