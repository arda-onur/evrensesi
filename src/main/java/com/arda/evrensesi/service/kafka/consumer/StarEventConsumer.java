package com.arda.evrensesi.service.kafka.consumer;

import com.arda.evrensesi.event.StarCreatedEvent;
import com.arda.evrensesi.mapper.search.StarDocumentMapper;
import com.arda.evrensesi.model.document.StarDocument;
import com.arda.evrensesi.repository.StarESRepository;
import com.arda.evrensesi.service.StarIndexStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StarEventConsumer {
    private final StarESRepository starESRepository;
    private final StarIndexStatusService starIndexStatusService;

    public StarEventConsumer(StarESRepository starESRepository, StarIndexStatusService starIndexStatusService) {
        this.starESRepository = starESRepository;
        this.starIndexStatusService = starIndexStatusService;
    }

    @KafkaListener(topics = "star-created-topic", groupId = "evrensesi-group")
    public void consume(StarCreatedEvent event) {
        try {
            log.info("Consuming event. starId={}", event.id());

            StarDocument starDocument = StarDocumentMapper.toDocument(event);
            starESRepository.save(starDocument);

            starIndexStatusService.markIndexed(event.id());

            log.info("Star successfully indexed into Elasticsearch. starId={}", event.id());
        } catch (Exception e) {
            log.error("Failed to consume StarCreatedEvent. starId={}", event.id(), e);
            throw e;
        }
    }
}
