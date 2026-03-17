package com.arda.evrensesi.listener;

import com.arda.evrensesi.event.StarCreatedEvent;
import com.arda.evrensesi.mapper.search.StarDocumentMapper;
import com.arda.evrensesi.model.document.StarDocument;
import com.arda.evrensesi.repository.StarESRepository;
import com.arda.evrensesi.service.StarIndexStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class StarCreatedListener {
    private final StarESRepository starESRepository;
    private final StarIndexStatusService starIndexStatusService;

    public StarCreatedListener(StarESRepository starESRepository, StarIndexStatusService starIndexStatusService) {
        this.starESRepository = starESRepository;
        this.starIndexStatusService = starIndexStatusService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleStarCreatedEvent(StarCreatedEvent starCreatedEvent) {
        try {
            log.info("Starting Elasticsearch indexing for starId={}", starCreatedEvent.id());

            StarDocument starDocument = StarDocumentMapper.toDocument(starCreatedEvent);
            starESRepository.save(starDocument);

            starIndexStatusService.markIndexed(starCreatedEvent.id());

            log.info("Star successfully indexed into Elasticsearch. starId={}", starCreatedEvent.id());
        } catch (Exception ex) {
            log.error("Elasticsearch indexing failed for starId={}", starCreatedEvent.id(), ex);
        }
    }

}
