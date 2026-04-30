package com.arda.evrensesi.publisher;

import com.arda.evrensesi.event.StarCreatedEvent;
import com.arda.evrensesi.model.entity.OutboxEvent;
import com.arda.evrensesi.repository.OutboxEventRepository;
import com.arda.evrensesi.service.kafka.producer.StarEventProducer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final StarEventProducer starEventProducer;
    private final JsonMapper jsonMapper;

    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> events = outboxEventRepository.findTop50BySentFalse();

        for (OutboxEvent outboxEvent : events) {
            try {
                if (!"StarCreatedEvent".equals(outboxEvent.getEventType())) {
                    log.warn("Unsupported outbox event type. eventType={}", outboxEvent.getEventType());
                    continue;
                }

                StarCreatedEvent event = jsonMapper.readValue(
                        outboxEvent.getPayload(),
                        StarCreatedEvent.class
                );

                starEventProducer.sendStarCreatedEvent(event);

                outboxEvent.markAsSent();

                log.info("Outbox event sent. outboxEventId={}", outboxEvent.getId());

            } catch (Exception e) {
                log.error("Failed to publish outbox event. outboxEventId={}", outboxEvent.getId(), e);
            }
        }
    }
}