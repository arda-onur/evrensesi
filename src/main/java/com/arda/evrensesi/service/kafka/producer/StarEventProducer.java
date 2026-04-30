package com.arda.evrensesi.service.kafka.producer;

import com.arda.evrensesi.event.StarCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StarEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendStarCreatedEvent(StarCreatedEvent event) {
        try {
            log.info("Sending StarCreatedEvent to Kafka. starId={}", event.id());

                kafkaTemplate
                        .send("star-created-topic", event.id().toString(), event)
                        .get();

            } catch (Exception e) {
                throw new RuntimeException("Failed to send StarCreatedEvent to Kafka", e);
            }
    }
}
