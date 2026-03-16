package com.arda.evrensesi.listener;

import com.arda.evrensesi.event.StarCreatedEvent;
import com.arda.evrensesi.mapper.search.StarDocumentMapper;
import com.arda.evrensesi.model.document.StarDocument;
import com.arda.evrensesi.repository.StarESRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class StarCreatedListener {
    private final StarESRepository starESRepository;

    public StarCreatedListener(StarESRepository starESRepository) {
        this.starESRepository = starESRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleStarCreatedEvent(StarCreatedEvent starCreatedEvent){
        StarDocument starDocument = StarDocumentMapper.toDocument(starCreatedEvent);
        starESRepository.save(starDocument);

    }
}
